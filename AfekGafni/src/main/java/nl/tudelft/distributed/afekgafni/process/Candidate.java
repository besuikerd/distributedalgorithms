package nl.tudelft.distributed.afekgafni.process;

import nl.tudelft.distributed.afekgafni.message.Ack;
import nl.tudelft.distributed.afekgafni.message.IMessage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Candidate extends IProcess {
  protected Candidate(int nodeId) throws RemoteException {
    super(nodeId);
  }

  public void sendWhatever() {
    Iterate over ordinary
            bool ack = roep receive func aan
            if (!ack)
              kill self
  }


  public void send(int nodeId, IMessage message) {
    Object o = null;
    try {
      o = Naming.lookup(getRemote(nodeId));
      if (o instanceof IProcess) {
        IMessage m = new Ack();
        IProcess that = (IProcess)o;
        that.receive(m);
      }
    } catch (NotBoundException | MalformedURLException | RemoteException e) {
      e.printStackTrace();
    }
  }
}
