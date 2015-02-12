package nl.tudelft.distributed.birmanschiperstephenson;

import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public abstract class AbstractEndpoint implements IEndpoint{
	
	@Override
	public void broadcast(Object message) {
		
	}
}
