import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import static akka.pattern.Patterns.ask;
import static akka.actor.SupervisorStrategy.resume;

import akka.japi.pf.DeciderBuilder;
import messages.ProductPriceRequest;
import messages.ProductPriceResponse;
import messages.ResponseWithCount;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends AbstractActor {
    private long id = 0;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ProductPriceRequest.class, req -> {
                    List<ProductPriceResponse> prices = new ArrayList<>();
                    AtomicInteger count = new AtomicInteger(-1);

                    log.info("Server got client request");

                    CompletableFuture<Object> dbFuture = ask(getContext().actorOf(Props.create(DbActor.class)), req, Duration.ofMillis(300)).toCompletableFuture();
                    CompletableFuture<Object> future1 = ask(getContext().actorOf(Props.create(PriceChecker.class)), req, Duration.ofMillis(300)).toCompletableFuture();
                    CompletableFuture<Object> future2 = ask(getContext().actorOf(Props.create(PriceChecker.class)), req, Duration.ofMillis(300)).toCompletableFuture();

                    dbFuture = dbFuture.whenComplete((res, err) -> {
                        if (err == null) {
                            Integer response = (Integer) res;
                            count.set(response);
                        }
                    });

                    future1 = future1.whenComplete((res, ex) -> {
                        if (ex == null) {
                            ProductPriceResponse response = (ProductPriceResponse) res;
                            log.info("Server got response (product = {}, price = {})",
                                    response.getProductName(), response.getPrice());
                            prices.add(response);
                        } else {
                            log.info("Timeout reached...");
                        }
                    });

                    future2 = future2.whenComplete((res, ex) -> {
                        if (ex == null) {
                            ProductPriceResponse response = (ProductPriceResponse) res;
                            log.info("Server got response (product = {}, price = {})",
                                    response.getProductName(), response.getPrice());
                            prices.add(response);
                        } else {
                            log.info("Timeout reached...");
                        }
                    });

                    CompletableFuture.allOf(future1, future2, dbFuture)
                            .whenComplete(
                                    (res, ex) -> {
                                        if (prices.size() == 0) {
                                            ProductPriceResponse ppr = new ProductPriceResponse(req.getProductName(), -1, getSelf());
                                            String message = "No responses for " + req.getProductName();
                                            req.replyTo.tell(new ResponseWithCount(ppr, count.get(), message), getSelf());

                                        } else {
                                            ProductPriceResponse ppr = Collections.min(prices, Comparator.comparingInt(ProductPriceResponse::getPrice));
                                            req.replyTo.tell(new ResponseWithCount(ppr, count.get(), ""), getSelf());
                                        }
                                    }
                            );
                })
                .matchAny(any -> log.info("Got unrecognizable message"))
                .build();
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                10,
                Duration.ofSeconds(30),
                DeciderBuilder.matchAny(any -> (SupervisorStrategy.Directive) resume())
                        .build()
        );
    }
}
