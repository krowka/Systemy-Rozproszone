package messages;

import akka.actor.ActorRef;

public class ProductPriceRequest {
    private final String productName;
    public final ActorRef replyTo;

    public ProductPriceRequest(String productName, ActorRef replyTo) {
        this.productName = productName;
        this.replyTo = replyTo;
    }

    public String getProductName() {
        return productName;
    }
}
