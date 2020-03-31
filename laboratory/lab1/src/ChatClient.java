import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class ChatClient {
    private Socket socket;
    private MulticastSocket multicastSocket;
    private InetAddress groupAddress;
    private DatagramSocket datagramSocket;
    private InetAddress serverAddress;
    private Integer serverPort = 12345;
    private Integer groupPort = 8888;
    private PrintWriter out;
    private BufferedReader in;
    private Scanner scanner;
    private String name;
    private ReadingThreadTcp readingThreadTcp;
    private ReadingThreadUdp readingThreadUdp;
    private ReadingThreadMulticast readingThreadMulticast;
    private WritingThread writingThread;

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        chatClient.execute();
    }

    public void execute() {
        initialize();
        readingThreadTcp = new ReadingThreadTcp();
        readingThreadUdp = new ReadingThreadUdp();
        readingThreadMulticast = new ReadingThreadMulticast();
        writingThread = new WritingThread();
        readingThreadTcp.start();
        readingThreadUdp.start();
        readingThreadMulticast.start();
        writingThread.start();
    }

    private void initialize() {
        try {
            socket = new Socket("127.0.0.1", serverPort);
            multicastSocket = new MulticastSocket(8888);
            groupAddress = InetAddress.getByName("230.0.0.0");
            multicastSocket.joinGroup(groupAddress);
            datagramSocket = new DatagramSocket();
            serverAddress = InetAddress.getByName("localhost");
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReadingThreadTcp extends Thread {
        private final AtomicBoolean running = new AtomicBoolean(false);

        public void run() {
            try {
                running.set(true);
                while (running.get()) {
                    String receivedMessage = in.readLine();
                    if (receivedMessage != null)
                        System.out.println(receivedMessage);
                }
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void kill() {
            this.running.set(false);

        }
    }

    private class ReadingThreadUdp extends Thread {
        private final AtomicBoolean running = new AtomicBoolean(false);

        public void run() {
            try {
                running.set(true);
                while (running.get()) {
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    datagramSocket.receive(receivePacket);
                    String message = new String(receivePacket.getData());
                    System.out.println(message);
                }
                in.close();
                datagramSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void kill() {
            this.running.set(false);
        }
    }

    private class ReadingThreadMulticast extends Thread {
        private final AtomicBoolean running = new AtomicBoolean(false);

        public void run() {
            try {
                running.set(true);
                while (running.get()) {
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    multicastSocket.receive(receivePacket);
                    String message = new String(receivePacket.getData());
                    String name = message.substring(message.indexOf('[') + 1, message.indexOf(']'));
                    if (!name.equals(ChatClient.this.name))
                        System.out.println(message);
                }
                in.close();
                multicastSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void kill() {
            this.running.set(false);
        }
    }

    private class WritingThread extends Thread {
        private byte[] sendBuffer;

        public void run() {
            scanner = new Scanner(System.in);
            System.out.println("Enter your name:");
            name = scanner.nextLine();
            out.println(name);
            sendDatagram("--register", 'U');
            String message;

            while (true) {
                message = scanner.nextLine();
                if (message.equals("--close")) {
                    break;
                }

                if (message.startsWith("@U ") || message.startsWith("@M ")) {
                    // UDP
                    String filename = message.split("\\s+")[1];
                    File f = new File(filename);
                    if (f.exists() && !f.isDirectory()) {
                        String text = "[" + name + "]\n" + getTextFromFile(filename);
                        Character type = message.charAt(1);
                        sendDatagram(text, type);
                    }

                } else {
                    // TCP
                    out.println(message);
                }
            }
            // client typed "--close", so running thread needs to be stopped
            readingThreadTcp.kill();
            readingThreadUdp.kill();
            readingThreadMulticast.kill();
        }

        private String getTextFromFile(String filename) {
            StringBuilder stringBuilder = new StringBuilder();
            try (Stream<String> stream = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)) {
                stream.forEach(line -> stringBuilder.append(line).append("\n"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private void sendDatagram(String message, Character type) {
            try {
                sendBuffer = message.getBytes();
                DatagramPacket datagramPacket;
                switch (type) {
                    case 'U':
                        datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
                        datagramSocket.send(datagramPacket);
                        break;
                    case 'M':
                        datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, groupAddress, groupPort);
                        datagramSocket.send(datagramPacket);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}