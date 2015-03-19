package nl.tudelft.distributed.afekgafni.process;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

abstract public class AbstractProcess<A> extends UnicastRemoteObject implements IProcess<A> {
	private static final long serialVersionUID = 1L;
	protected int nodeId;

	protected AbstractProcess(int nodeId) throws RemoteException {
		super();
		this.nodeId = nodeId;
	}

	public static String getRemote(String name, int nodeId) {
		return "rmi://localhost:1337/" + name + "_" + nodeId;
	}

	public void log(String message) {
		System.out.println("[" + this.getClass().getSimpleName() + " " + nodeId + "] " + message);
	}
}
