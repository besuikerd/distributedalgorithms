package nl.tudelft.distributed.afekgafni;

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

  public void receive(Message message) {
    System.out.println(String.format("[%d] Received: %s", nodeId, message.toString()));
  }

  public void send(int nodeId, Message message) {
    Object o = null;
    try {
      o = Naming.lookup(getRemote(nodeId));
      if (o instanceof Node) {
        Message m = new Message();
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
