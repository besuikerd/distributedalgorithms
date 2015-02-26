package nl.tudelft.distributed.afekgafni;

import nl.tudelft.distributed.afekgafni.message.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Node extends UnicastRemoteObject implements Remote {
  private int nodeId;

  protected Node(int nodeId) throws RemoteException {
    super();
    this.nodeId = nodeId;
  }

  public void receive(IMessage message) {
    System.out.println(String.format("[%d] Received: %s", nodeId, message.toString()));
  }

  public void send(int nodeId, IMessage message) {
    Object o = null;
    try {
      o = Naming.lookup(getRemote(nodeId));
      if (o instanceof Node) {
        IMessage m = new Ack();
        Node that = (Node)o;
        that.receive(m);
      }
    } catch (NotBoundException | MalformedURLException | RemoteException e) {
      e.printStackTrace();
    }
  }

  public static String getRemote(int nodeId) {
    return "rmi://localhost:1337/" + Node.class.getName() + "_" + nodeId;
  }
}
