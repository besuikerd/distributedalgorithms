package nl.tudelft.distributed.birmanschiperstephenson;

import java.util.HashMap;
import java.util.Map;

public class InOrderEndpoint implements IEndpoint, Runnable{

	private IEndpoint delegate;
	private Map<Integer,Integer> messages;
	private int numberOfMessages;
	
	public InOrderEndpoint(IEndpoint delegate, int numberOfMessages) {
		this.delegate = delegate;
		this.messages = new HashMap<Integer, Integer>();
		this.numberOfMessages = numberOfMessages;
		
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
    public void deliver(Message message) {
        synchronized (messages) {
            Tuple2<Integer, Integer> tuple = (Tuple2<Integer, Integer>) message.getMessage();
            if (messages.containsKey(tuple._1)) {
                Integer current = messages.get(tuple._1);
                Integer expected = current + 1;
                if (tuple._2 != expected.intValue()) {
                    System.err.println(String.format("message received in invalid order, expected: %d, got: %d", expected, tuple._2));
                } else {
                    messages.put(tuple._1, tuple._2);
                }
            } else if (tuple._2 == 0) {
                messages.put(tuple._1, tuple._2);
            } else {
                System.err.println(String.format("message received in invalid order, expected: %d, got: %d", 0, tuple._2));
            }
        }
        delegate.deliver(message);
    }

	@Override
	public void broadcast(Object message) {
		delegate.broadcast(message);
	}

	@Override
	public int[] vectorClock() {
		return delegate.vectorClock();
	}
	
	@Override
	public int getNodeId() {
		return delegate.getNodeId();
	}

	@Override
	public void run() {
		for(int i = 0 ; i < numberOfMessages ; i++){
			broadcast(Tuple2.create(delegate.getNodeId(), i));
		}
	}
}