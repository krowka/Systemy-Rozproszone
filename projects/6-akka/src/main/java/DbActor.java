import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import messages.ProductPriceRequest;

import java.sql.*;

public class DbActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ProductPriceRequest.class, req -> {
                    String url = "jdbc:sqlite:request.db";
                    String selectQuery = "SELECT request_number FROM products_request WHERE product_name = '" + req.getProductName() + "'";

                    try (Connection connection = DriverManager.getConnection(url);
                         Statement statement = connection.createStatement();
                         ResultSet resultSet = statement.executeQuery(selectQuery)) {

                        if (resultSet.next()) {
                            int requestCount = resultSet.getInt("request_number");
                            getSender().tell(requestCount, getSelf());
                            String sqlUpdate = "UPDATE products_request SET request_number = " + (requestCount + 1) +
                                    " WHERE product_name = '" + req.getProductName() + "'";
                            statement.executeUpdate(sqlUpdate);
                        } else {
                            getSender().tell(0, getSelf());
                            String sqlInsert = "INSERT INTO products_request VALUES ('" + req.getProductName() + "', 1)";
                            statement.executeUpdate(sqlInsert);
                        }
                    }
                })
                .build();
    }
}
