package nl.tudelft.distributed.afekgafni.process;

import nl.tudelft.distributed.afekgafni.message.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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
