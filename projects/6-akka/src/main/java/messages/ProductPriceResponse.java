package messages;

import akka.actor.ActorRef;

public class ProductPriceResponse {
    private final String productName;
    private final int price;
    private final ActorRef sender;

    public ProductPriceResponse(String productName, int price, ActorRef sender) {
        this.productName = productName;
        this.price = price;
        this.sender = sender;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public ActorRef getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "product = " + productName + ", price = " + price;
    }
}
