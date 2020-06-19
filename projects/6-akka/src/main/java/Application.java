import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import messages.ClientRequest;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Application {
    private final ActorSystem system;
    private final ActorRef server;
    private Map<String, ActorRef> clients;
    private final Scanner scanner;

    public Application() {
        this.system = ActorSystem.create("system");
        this.server = system.actorOf(Props.create(Server.class), "server");
        this.clients = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new Application().run();
    }

    private void run() {
        clients.put("c1", system.actorOf(Props.create(Client.class), "client1"));
        clients.put("c2", system.actorOf(Props.create(Client.class), "client2"));
        clients.put("c3", system.actorOf(Props.create(Client.class), "client3"));

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("quit"))
                break;

            if (input.equals("simulate")) {
                simulate();
            } else if (input.equals("ls")) {
                System.out.println("Available clients: " + clients.keySet());
            } else {
                String[] split = input.split("\\W+");
                System.out.println(Arrays.toString(split));
                if (split.length != 2) {
                    System.out.println("Invalid number of arguments. Expected 2, got " + split.length);
                    continue;
                }
                String actorName = split[0];
                String productName = split[1];

                if (!clients.containsKey(actorName)) {
                    System.out.println("Actor with name: " + actorName + " doesn't exist.");
                    continue;
                }
                clients.get(actorName).tell(new ClientRequest(server, productName), ActorRef.noSender());
            }
        }
    }

    private void simulate() {
        List<ActorRef> refs = new ArrayList<>(clients.values());
        Random random = new Random();

        Stream.of("laptop", "monitor", "klawiatura", "sluchawki", "myszka", "PC", "dysk", "pendrive", "kamerka")
                .forEach(s -> refs.get(random.nextInt(clients.size()))
                        .tell(new ClientRequest(server, s), ActorRef.noSender()));
    }
}