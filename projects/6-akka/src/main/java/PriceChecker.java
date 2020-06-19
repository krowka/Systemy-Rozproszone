import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import messages.ProductPriceRequest;
import messages.ProductPriceResponse;

import java.util.Random;

public class PriceChecker extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Random random = new Random();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ProductPriceRequest.class, req -> {
                    int timeToSleep = random.nextInt(400) + 100;
                    Thread.sleep(timeToSleep);
                    int price = random.nextInt(10) + 1;
                    getSender().tell(new ProductPriceResponse(req.getProductName(), price, getSelf()), getSelf());
                })
                .matchAny(any -> log.info("Got unrecognizable message"))
                .build();
    }
}
