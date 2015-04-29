package agreement;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IProcess<A> extends Remote, Serializable {
    void receive(A msg) throws RemoteException;
}