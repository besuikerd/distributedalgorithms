package nl.tudelft.distributed.afekgafni.process;

import nl.tudelft.distributed.afekgafni.message.Ack;
import nl.tudelft.distributed.afekgafni.message.IMessage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Ordinary extends IProcess {

  protected Ordinary(int nodeId) throws RemoteException {
    super(nodeId);
  }

  public boolean receive(IMessage message) {
    System.out.println(String.format("[%d] Received: %s", nodeId, message.toString()));
    return true;
  }
}
