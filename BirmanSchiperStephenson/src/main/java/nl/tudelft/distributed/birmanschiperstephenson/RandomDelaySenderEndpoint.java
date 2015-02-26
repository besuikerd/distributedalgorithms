package nl.tudelft.distributed.birmanschiperstephenson;

import java.util.Random;


public class RandomDelaySenderEndpoint extends AbstractEndpoint implements Runnable {

    private int numberOfMessages;
    private Random random;

    public RandomDelaySenderEndpoint(int nodeId, String[] remotes, int numberOfMessages) {
        super(nodeId, remotes);
        this.numberOfMessages = numberOfMessages;
        this.random = new Random();
    }

    @Override
    public void deliver(Message message) {
      //System.out.println("[" + nodeId + "] " + message.getMessage().toString());
    }

    @Override
    public void run() {
        Thread.currentThread().setName(String.format("Node[%d]", nodeId));
        for (int messageNumber = 0; messageNumber < numberOfMessages; messageNumber++) {
            try {
                Thread.sleep(random.nextInt(5000) + 100);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            broadcast(String.format("%d => #%d", nodeId, messageNumber));
        }
    }

}
