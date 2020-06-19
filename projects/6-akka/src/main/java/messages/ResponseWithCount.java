package messages;

import akka.actor.ActorRef;

public class ResponseWithCount {
    private final ProductPriceResponse response;
    private final int count;
    private final String message;

    public ResponseWithCount(ProductPriceResponse response, int count, String message) {
        this.response = response;
        this.count = count;
        this.message = message;
    }

    public String getProductName() {
        return response.getProductName();
    }

    public int getPrice() {
        return response.getPrice();
    }

    public ActorRef getSender() {
        return response.getSender();
    }

    public int getCount() {
        return count;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        String countString = count < 0 ? "" : ", count = " + count;
        return "product = " + getProductName() + ", price = " + getPrice() + countString;
    }
}
