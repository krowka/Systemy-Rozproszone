import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.*;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.data.Stat;

public class DataMonitor implements StatCallback, AsyncCallback.Children2Callback {
    private final ZooKeeper zk;
    private int prevCount = -1;
    private final String zNode;
    private final String[] exec;
    private Process process = null;

    DataMonitor(String zNode, ZooKeeper zk, String[] exec) {
        this.zk = zk;
        this.zNode = zNode;
        this.exec = exec;
    }

    public void startWatch() {
        zk.exists(zNode, true, this, null);
        this.countNodes(zNode);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (rc == KeeperException.Code.OK.intValue()) {
            if (process == null) {
                System.out.println("Created /z node");
                zk.getChildren(zNode, true, this, null);

                ProcessBuilder pb = new ProcessBuilder();
                pb.command(exec);

                try {
                    this.process = pb.start();
                } catch (IOException e) {
                    System.err.println("Error starting executable");
                    e.printStackTrace();
                }
            }
        } else if (rc == KeeperException.Code.NONODE.intValue()) {
            if (this.process != null) {
                System.out.println("Deleted /z node");
                this.process.destroy();
                this.process = null;
            }
        } else {
            System.err.println("Error in processResult: " + rc);
        }

        zk.exists(zNode, true, this, null);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> list, Stat stat) {
        int count = this.countNodes(zNode);
        if (count != prevCount) {
            prevCount = count;
            if (rc == KeeperException.Code.OK.intValue()) {
                System.out.println("Number of children has changed to: " + (count - 1));
            }
        }
    }

    private int countNodes(String name) {
        zk.getChildren(name, true, this, null);
        int sum = 1;

        try {
            sum += zk.getChildren(name, false).stream().mapToInt(ch -> countNodes(name + "/" + ch)).sum();

        } catch (KeeperException.NoNodeException ex) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sum;
    }
}