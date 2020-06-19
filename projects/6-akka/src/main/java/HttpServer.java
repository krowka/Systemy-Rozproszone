import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import messages.ProductPriceRequest;
import messages.ResponseWithCount;
import org.jsoup.Jsoup;


import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static akka.http.javadsl.server.PathMatchers.segment;

public class HttpServer extends AllDirectives {
    private ActorRef server;
    private ActorSystem system;
    private Materializer materializer;

    public HttpServer(ActorSystem system, Materializer materializer) {
        this.system = system;
        this.server = system.actorOf(Props.create(Server.class), "server");
        this.materializer = materializer;
    }

    public static void main(String[] args) throws Exception {
        // boot up server using the route as defined below
        ActorSystem system = ActorSystem.create("routes");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        //In order to access all directives we need an instance where the routes are define.
        HttpServer app = new HttpServer(system, materializer);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return

        binding
                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    public Route createRoute() {
        return concat(
                path(segment("price").slash(segment()), product ->
                        get(() -> {
                            CompletionStage<Object> reply = Patterns.askWithReplyTo(
                                    this.server,
                                    actorRef -> new ProductPriceRequest(product, actorRef),
                                    Duration.ofSeconds(1));

                            return onSuccess(reply, (resp) -> {
                                ResponseWithCount response = (ResponseWithCount) resp;
                                String message = response.getMessage();
                                if (message.equals(""))
                                    return complete(response.toString());
                                else
                                    return complete(message);
                            });
                        })
                ),
                path(segment("review").slash(segment()), name ->
                        get(() -> {
                            final CompletionStage<Object> query = Http.get(system)
                                    .singleRequest(HttpRequest.create("https://www.opineo.pl/?szukaj=" + name + "&s=2"))
                                    .thenCompose(response -> response.entity().toStrict(5000, materializer))
                                    .thenApply(entity -> Jsoup.parse(entity.getData().utf8String())
                                            .body()
                                            .getElementById("page")
                                            .getElementById("content")
                                            .getElementById("screen")
                                            .getElementsByClass("pls")
                                            .get(0)
                                            .getElementsByClass("shl_i pl_i")
                                            .get(0)
                                            .getElementsByClass("pl_attr")
                                            .get(0)
                                            .getElementsByTag("li")
                                            .eachText()
                                            .stream()
                                            .filter(s -> !s.equals("..."))
                                            .collect(Collectors.joining("\n"))
                                    );
                            return onSuccess(query, result -> complete(result.toString()));
                        })
                )
        );
    }

}
