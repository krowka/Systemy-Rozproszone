import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Z2_RemoteActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    System.out.println("Remote actor got message: " + message);
                    getSender().tell(message.toUpperCase(), getSelf());
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
