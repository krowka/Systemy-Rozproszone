import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Admin {
    private static final String EXCHANGE_NAME = "space";
    private Channel channel;
    private String adminName;
    private BufferedReader br;
    private final String[] modes = {"agency", "transporter", "all"};


    public static void main(String[] args) throws Exception {
        Admin admin = new Admin();
        admin.initialize();
        admin.run();
    }

    private void initialize() throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your name: ");
        adminName = br.readLine();

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        channel.queueDeclare(adminName, false, false, false, null);
        channel.queueBind(adminName, EXCHANGE_NAME, "#");

        channel.basicConsume(adminName, false, createConsumer(adminName));
    }

    private void run() throws IOException {
        System.out.println("To send information to specific group, write: <X> important message for X group \n" +
                "Where X is " + String.join(", ", modes));
        while (true) {
            System.out.print("ADMIN >> ");
            String message = br.readLine();

            if (message.equals("exit"))
                break;

            Map<String, Object> headers = new HashMap<>();
            headers.put("sender", adminName);
            AMQP.BasicProperties props = new AMQP.BasicProperties().builder().type("info").headers(headers).build();

            String key = message.substring(message.indexOf('<') + 1, message.indexOf('>'));
            message = message.substring(message.indexOf('>') + 1);

            if (Arrays.asList(modes).contains(key)) {
                if (key.equals("all")) {
                    channel.basicPublish(EXCHANGE_NAME, "agency", props, message.getBytes());
                    channel.basicPublish(EXCHANGE_NAME, "transporter", props, message.getBytes());
                } else {
                    channel.basicPublish(EXCHANGE_NAME, key, props, message.getBytes());
                }
            } else {
                System.out.println("Valid modes are: " + String.join(", ", modes));
            }
        }
    }


    private Consumer createConsumer(String adminName) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                if (!properties.getHeaders().get("sender").toString().equals(adminName)) {
                    System.out.println("[SYSTEM INFO] " + message);
                }
            }
        };
    }
}
