package messages;

import akka.actor.ActorRef;

public class ClientRequest {
    public final ActorRef server;
    private final String productName;

    public ClientRequest(ActorRef server, String productName) {
        this.server = server;
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
}
