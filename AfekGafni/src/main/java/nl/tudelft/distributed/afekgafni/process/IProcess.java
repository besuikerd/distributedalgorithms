package nl.tudelft.distributed.afekgafni.process;

public interface IProcess<A> {
	public void receive(A msg);
}
