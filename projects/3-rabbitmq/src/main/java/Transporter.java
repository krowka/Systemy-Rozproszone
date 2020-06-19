import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Transporter {
    private final String EXCHANGE_NAME = "space";
    private Channel channel;
    private final String transporterName;
    private final Service[] services;

    public Transporter(String transporterName, Service[] services) throws Exception {
        this.transporterName = transporterName;
        this.services = services;
        initialize();
    }

    private void initialize() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        for (Service service : services) {
            channel.queueDeclare(service.name().toLowerCase(), false, false, false, null);
            channel.queueBind(service.name().toLowerCase(), EXCHANGE_NAME, service.name().toLowerCase());
            channel.basicConsume(service.name().toLowerCase(), false, createConsumer()); // autoAck = false
        }

        channel.queueDeclare(transporterName, false, false, false, null);
        channel.queueBind(transporterName, EXCHANGE_NAME, "transporter");
        channel.basicConsume(transporterName, false, createConsumer());

        System.out.println(transporterName + " is waiting for requests.");
    }

    private Consumer createConsumer() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                if (properties.getType().equals("request")) {
                    System.out.println("Processing " + message);

                    channel.basicAck(envelope.getDeliveryTag(), false);

                    Map<String, Object> headers = new HashMap<>();
                    headers.put("sender", transporterName);
                    AMQP.BasicProperties props = new AMQP.BasicProperties().builder().type("request").headers(headers).build();
                    String ackInfo = message.substring(0, 1).toUpperCase() + message.substring(1) + " done";

                    channel.basicPublish(EXCHANGE_NAME, properties.getHeaders().get("sender").toString(), props, ackInfo.getBytes());
                } else {
                    System.out.println("[INFO] " + message);
                }
            }
        };
    }
}