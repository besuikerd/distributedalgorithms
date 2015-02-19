package nl.tudelft.distributed.birmanschiperstephenson;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jasper on 2/19/2015.
 */
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
        System.out.println("[" + nodeId + "] " + message.getMessage().toString());
    }

    @Override
    public void broadcast(Object message) {
        vectorClock.increment(nodeId);
        broadcastList.add(new Tuple3<Integer, Object, VectorClock>(nodeId, message, vectorClock.copy()));
        if (broadcastList.size() == numberOfMessages) {
            for (int i = 0; i < numberOfMessages; i++) {
                broadcast();
            }
        }
    }

    public void broadcast() {
        int messageCount = broadcastList.size();

        Tuple3<Integer, Object, VectorClock> message = broadcastList.remove(random.nextInt(messageCount));

        System.out.println("[" + message._1 + "] Sending " + message._2 + " as " + message._3.toString());
        int i = 0;
        for (String remote : remotes) {
            // Don't broadcast to self
            if (i++ == nodeId) {
                continue;
            }
            try {
                Object o = Naming.lookup(remote);
                if (o instanceof IEndpointBuffer) {
                    Message m = new Message(message._1, i, message._3, message._2);
                    ((IEndpointBuffer) o).receive(m);
                }
            } catch (MalformedURLException | RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}
