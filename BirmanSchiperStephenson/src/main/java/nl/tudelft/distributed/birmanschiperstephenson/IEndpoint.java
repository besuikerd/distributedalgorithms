package nl.tudelft.distributed.birmanschiperstephenson;

public interface IEndpoint {
    public void deliver(Message message);

    public void broadcast(Object message);

    public VectorClock getClock();

    public int getNodeId();
}