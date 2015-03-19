package nl.tudelft.distributed.afekgafni.process;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

abstract public class AbstractProcess<A> extends UnicastRemoteObject implements Remote, IProcess<A> {
	private static final long serialVersionUID = 1L;
	protected int nodeId;

	protected AbstractProcess(int nodeId) throws RemoteException {
		super();
		this.nodeId = nodeId;
	}

	public static String getRemote(int nodeId) {
		return "rmi://localhost:1337/" + IProcess.class.getName() + "_" + nodeId;
	}
	
	public String getRemote() {
		return getRemote(nodeId);
	}
}
