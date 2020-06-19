import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Z2_LocalActor extends AbstractActor {
    private final String remotePath = "akka://remote_system@127.0.0.1:2552/user/remote";
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (getSender().path().toString().equals(remotePath)) {
                        System.out.println("Local actor got response: " + message);
                    } else {
                        System.out.println("Local actor got message: " + message);
                        getContext().actorSelection(remotePath).tell(message, getSelf());
                    }
                })
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }
}
