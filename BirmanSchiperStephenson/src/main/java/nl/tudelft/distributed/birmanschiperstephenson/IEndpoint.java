package nl.tudelft.distributed.birmanschiperstephenson;

public interface IEndpoint {
	public void deliver(Object message);
	public void broadcast(Object message);
	public int[] vectorClock();
	public int getNodeId();
}