package nl.tudelft.distributed.birmanschiperstephenson;

import java.util.HashMap;
import java.util.Map;

public class InOrderEndpoint implements IEndpoint, Runnable {

    private final Map<Integer, Integer> messages = new HashMap<>();
  private final int numberOfProcesses;
  private IEndpoint delegate;
    private int numberOfMessages;

  public InOrderEndpoint(IEndpoint delegate, int numberOfMessages, int numberOfProcesses) {
        this.delegate = delegate;
        this.numberOfMessages = numberOfMessages;
    this.numberOfProcesses = numberOfProcesses;

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void deliver(Message message) {
        synchronized (messages) {
            //System.out.println("[" + getNodeId() + "] Got clock " + message.getClock() + ", need " + getClock() + "; " + messages.size());
            delegate.deliver(message);
            Tuple2<Integer, Integer> tuple = (Tuple2<Integer, Integer>) message.getMessage();
            if (messages.containsKey(tuple._1)) {
                Integer current = messages.get(tuple._1);
                Integer expected = current + 1;
                if (tuple._2 != expected.intValue()) {
                    System.err.println(String.format("1 [%d] message received in invalid order, expected: %d, got: %d", tuple._1, expected, tuple._2));
                    System.exit(1);
                } else {
                    messages.put(tuple._1, tuple._2);
                }
            } else if (tuple._2 == 0) {
                messages.put(tuple._1, tuple._2);
            } else {
                System.err.println(String.format("2 [%d] message received in invalid order, expected: %d, got: %d", tuple._1, 0, tuple._2));
                System.exit(1);
            }
            checkAllMessagesReceived();
        }
    }

    @Override
    public void broadcast(Object message) {
        delegate.broadcast(message);
    }
    
    private void checkAllMessagesReceived(){
      for (int i = 0; i < numberOfProcesses; i++) {
        if (i != delegate.getNodeId() && (!messages.containsKey(i) || !messages.get(i).equals(numberOfMessages - 1))) {
          return;
    		}
    	}
    	System.out.println(String.format("[%d] Received all broadcasts: %s", delegate.getNodeId(), messages));
    }

    @Override
    public VectorClock getClock() {
        return delegate.getClock();
    }

    @Override
    public int getNodeId() {
        return delegate.getNodeId();
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfMessages; i++) {
            broadcast(Tuple2.create(delegate.getNodeId(), i));
        }
    }
}
