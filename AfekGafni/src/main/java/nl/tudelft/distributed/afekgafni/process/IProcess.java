package nl.tudelft.distributed.afekgafni.process;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

abstract public class IProcess extends UnicastRemoteObject implements Remote {
	protected int nodeId;

	protected IProcess(int nodeId) throws RemoteException {
		super();
		this.nodeId = nodeId;
	}

	public String getRemote(int nodeId) {
		return "rmi://localhost:1337/" + getClass().getName() + "_" + nodeId;
	}
}
