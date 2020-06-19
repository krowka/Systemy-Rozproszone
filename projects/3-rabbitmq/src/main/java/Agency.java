import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Agency {
    private final String EXCHANGE_NAME = "space";
    private final String agencyName;
    private Channel channel;
    private int requestId = 1;

    public Agency(String agencyName) throws Exception {
        this.agencyName = agencyName;
        initialize();
    }


    public void makeRequest(Service service) throws IOException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("sender", agencyName);
        String message = service.name().toLowerCase() + " request: " + agencyName + "-" + requestId++;
        AMQP.BasicProperties props = new AMQP.BasicProperties().builder().type("request").headers(headers).build();
        channel.basicPublish(EXCHANGE_NAME, service.name().toLowerCase(), props, message.getBytes());
    }

    private void initialize() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        for (Service service : Service.values()) {
            channel.queueDeclare(service.name().toLowerCase(), false, false, false, null);
            channel.queueBind(service.name().toLowerCase(), EXCHANGE_NAME, service.name().toLowerCase());
        }

        channel.queueDeclare(agencyName, false, false, false, null);
        channel.queueBind(agencyName, EXCHANGE_NAME, "agency");
        channel.queueBind(agencyName, EXCHANGE_NAME, agencyName);
        channel.basicConsume(agencyName, false, createConsumer()); // autoAck = false
    }

    private Consumer createConsumer() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[INFO] " + message);
                System.out.printf("[%s] >> ", agencyName);
            }
        };
    }
}