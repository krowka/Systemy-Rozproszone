import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private Set<Pair<InetAddress, Integer>> registeredClients = new HashSet<>();
    private Set<ClientHandler> clientHandlers = new HashSet<>();
    private Set<String> nicknames = new HashSet<>();
    private DatagramSocket datagramSocket;

    public void execute() {

        Thread tcpThread = new Thread() {
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(12345);
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        ClientHandler newClientHandler = new ClientHandler(clientSocket, ChatServer.this);
                        clientHandlers.add(newClientHandler);
                        newClientHandler.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread udpThread = new Thread() {
            public void run() {
                try {
                    datagramSocket = new DatagramSocket(12345);
                    while (true) {
                        byte[] receiveBuffer = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                        datagramSocket.receive(receivePacket);
                        String message = new String(receivePacket.getData());

                        if (message.contains("--register")) {
                            InetAddress address = receivePacket.getAddress();
                            int port = receivePacket.getPort();
                            Pair<InetAddress, Integer> pair = new Pair<>(address, port);
                            registeredClients.add(pair);
                        } else {
                            broadcastDatagram(message, receivePacket.getAddress(), receivePacket.getPort());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };

        tcpThread.start();
        udpThread.start();

        System.out.println("SERVER LISTENING ON PORT 12345");
    }


    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.execute();
    }

    public void broadcast(String message) {
        for (ClientHandler clientHandler : clientHandlers)
            clientHandler.sendToClient(message);
    }

    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (!sender.equals(clientHandler)) {
                clientHandler.sendToClient(message);
            }
        }
    }

    public void removeClientHandler(ClientHandler clientHandler) {
        this.clientHandlers.remove(clientHandler);
        this.nicknames.remove(clientHandler.getClientName());
        System.out.println("[INFO] " + clientHandler.getClientName() + " has left the chat");
    }

    public void addClientNickname(String nickname) {
        this.nicknames.add(nickname);
        System.out.println("[INFO] " + nickname + " has entered the chat");
    }

    public String connectedClients() {
        return nicknames.isEmpty() ? "[INFO] Chat is empty" : "ONLINE USERS: " + nicknames.toString();
    }

    private void broadcastDatagram(String message, InetAddress address, Integer port) {
        try {
            byte[] sendBuffer = message.getBytes();
            Pair<InetAddress, Integer> sender = new Pair<>(address, port);
            for (Pair<InetAddress, Integer> pair : registeredClients) {
                if (!sender.equals(pair)) {
                    DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, pair.getKey(), pair.getVal());
                    datagramSocket.send(packet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}