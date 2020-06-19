import java.io.*;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor implements Watcher {
    private final String zNode = "/z";
    private ZooKeeper zk;
    private DataMonitor dataMonitor;

    public Executor(String hostPort, String[] exec) {
        try {
            this.zk = new ZooKeeper(hostPort, 5000, this);
            this.dataMonitor = new DataMonitor(zNode, zk, exec);
            dataMonitor.startWatch();
        } catch (IOException e) {
            System.err.println("Error creating executor...");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Wrong arguments number");
            System.err.println("Executor hostPort program [args ...]");
            System.exit(-1);
        }
        String hostPort = args[0];
        String[] exec = new String[args.length - 1];
        System.arraycopy(args, 1, exec, 0, exec.length);

        Executor executor = new Executor(hostPort, exec);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter ls to see the list of nodes");
        while (true) {
            try {
                if (br.readLine().equals("ls"))
                    executor.listNodes();
            } catch (IOException e) {
                System.err.println("Error reading line...");
            }
        }
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
    }

    private void listNodes(List<String> nodes, String base) throws InterruptedException, KeeperException {
        for (String node : nodes) {
            String nodePath = base + "/" + node;
            System.out.println(nodePath);
            List<String> children = this.zk.getChildren(nodePath, false);
            listNodes(children, nodePath);
        }
    }

    private void listNodes() {
        try {
            List<String> children = this.zk.getChildren(zNode, false);
            System.out.println("Children of " + zNode);
            listNodes(children, zNode);

        } catch (InterruptedException | KeeperException e) {
            System.err.println("Cannot get children.");
        }
    }
}