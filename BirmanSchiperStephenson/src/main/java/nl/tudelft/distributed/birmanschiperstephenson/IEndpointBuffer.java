package nl.tudelft.distributed.birmanschiperstephenson;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IEndpointBuffer extends Remote {
    public void receive(Message message) throws RemoteException;
}