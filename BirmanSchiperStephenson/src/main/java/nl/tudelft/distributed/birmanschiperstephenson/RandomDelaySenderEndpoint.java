package nl.tudelft.distributed.birmanschiperstephenson;

public class RandomDelaySenderEndpoint extends AbstractEndpoint implements Runnable {

	private int nodeId;
	private int numberOfMessages;
	
	public RandomDelaySenderEndpoint(int nodeId, int numberOfMessages) {
		this.nodeId = nodeId;
		this.numberOfMessages = numberOfMessages;
	}

	@Override
	public void deliver(Object message) {
		System.out.println();
	}

	@Override
	public void run() {
		Thread.currentThread().setName(String.format("Node[%d]", nodeId));
		for(int messageNumber = 0 ; messageNumber < numberOfMessages ; messageNumber++){
			
		}
	}

}
