package nl.tudelft.distributed.afekgafni.process;

import nl.tudelft.distributed.afekgafni.message.Ack;
import nl.tudelft.distributed.afekgafni.message.IMessage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Candidate extends IProcess {
	private String[] remotes;

	protected Candidate(int nodeId, String[] remotes) throws RemoteException {
		super(nodeId);
		this.remotes = remotes;
	}

	public void send(int nodeId, IMessage message) {
		Object o = null;
		try {
			o = Naming.lookup(getRemote(nodeId));
			if (o instanceof Ordinary) {
				IMessage m = new Ack();
				Ordinary that = (Ordinary) o;
				that.receive(m);
			}
		} catch (NotBoundException | MalformedURLException | RemoteException e) {
			e.printStackTrace();
		}
	}
}
