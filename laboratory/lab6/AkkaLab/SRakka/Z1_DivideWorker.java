import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Z1_DivideWorker extends AbstractActor {
    private int count = 0;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    String result = divide(s);
                    getSender().tell("result: " + result, getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private String divide(String s) {
        String[] split = s.split("\\W+");
        int a = Integer.parseInt(split[1]);
        int b = Integer.parseInt(split[2]);
        return ((double) a / b) + " (operation count: " + ++count + ")";
    }
}
