package nl.tudelft.distributed.afekgafni.process;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IProcess<A> extends Remote, Serializable {
	public void receive(A msg) throws RemoteException;
}