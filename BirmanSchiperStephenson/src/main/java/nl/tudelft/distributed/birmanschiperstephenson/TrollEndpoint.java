package nl.tudelft.distributed.birmanschiperstephenson;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class TrollEndpoint extends AbstractEndpoint {

    private int numberOfMessages;
    private Random random;

    private ArrayList<Tuple3<Integer, Object, VectorClock>> broadcastList = new ArrayList<>();

    public TrollEndpoint(int nodeId, int numberOfMessages, String[] remotes) {
        super(nodeId, remotes);
        this.numberOfMessages = numberOfMessages;
        this.random = new Random();
    }

    @Override
    public void deliver(Message message) {
      //System.out.println("[" + nodeId + "] Received " + message.getMessage().toString());
    }

    @Override
    public void broadcast(Object message) {
        vectorClock.increment(nodeId);
        broadcastList.add(new Tuple3<>(nodeId, message, vectorClock.copy()));
        if (broadcastList.size() == numberOfMessages) {
          //System.out.println("[" + nodeId + "] Got all messages; broadcasting in random order");
            for (int i = 0; i < numberOfMessages; i++) {
                broadcast();
            }
        }
    }

    public void broadcast() {
        int messageCount = broadcastList.size();

        Tuple3<Integer, Object, VectorClock> message = broadcastList.remove(random.nextInt(messageCount));

      //System.out.println("[" + message._1 + "] Sending " + message._2 + " as " + message._3.toString());
        //int i = 0;
        for (int i = 0; i < remotes.length; i++) {
            String remote = remotes[i];
            // Don't broadcast to self
            if (i == nodeId) {
                continue;
            }
            try {
              //System.out.println(remote);
                Object o = Naming.lookup(remote);
                if (o instanceof IEndpointBuffer) {
                    if (message._1 != nodeId) {
                        System.err.println("WTF NOOB 1");
                    }
                    if (message._1 == i) {
                        System.err.println("WTF NOOB 2");
                    }
                    Message m = new Message(message._1, i, message._3, message._2);
                  System.out.println("[" + message._1 + "] Sending " + message._2 + " to " + i);
                    ((IEndpointBuffer) o).receive(m);
                } else {
                  System.err.println("Did not get endpoint");
                }
            } catch (MalformedURLException | RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}
