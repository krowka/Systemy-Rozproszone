import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import messages.ClientRequest;
import messages.ProductPriceRequest;

import messages.ResponseWithCount;

public class Client extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClientRequest.class, request -> request.server
                        .tell(new ProductPriceRequest(request.getProductName(), getSelf()), getSelf()))
                .match(ResponseWithCount.class, response -> {
                    String message = response.getMessage();
                    if (message.equals("")) {
                        log.info("Client got response: {}", response);
                    } else {
                        log.info(message);
                    }

                })
                .matchAny(any -> log.info("Got unrecognizable message"))
                .build();
    }
}
