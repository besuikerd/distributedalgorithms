package nl.tudelft.distributed.birmanschiperstephenson;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IEndpointBuffer extends Remote{
	public void receive(Object message, int sender, int[] vector) throws RemoteException;
}