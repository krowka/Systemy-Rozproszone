import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private ChatServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientName = in.readLine();
            sendToClient(server.connectedClients());
            server.addClientNickname(clientName);
            server.broadcast("[INFO] " + clientName + " has joined the chat.", this);

            String message;
            while (true) {
                message = in.readLine();
                if ("--close".equals(message))
                    break;
                server.broadcast(formatMessage(message), this);
            }

            in.close();
            out.close();
            clientSocket.close();
            server.removeClientHandler(this);
            server.broadcast("[INFO] " + clientName + " has left the chat.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String formatMessage(String message) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        String datetime = localDateTime.format(formatter);
        return String.format("[%s | %s]: %s", clientName, datetime, message);
    }

    public void sendToClient(String message) {
        out.println(message);
    }

    public String getClientName() {
        return this.clientName;
    }
}