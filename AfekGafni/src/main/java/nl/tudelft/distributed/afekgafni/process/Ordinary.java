package nl.tudelft.distributed.afekgafni.process;

import nl.tudelft.distributed.afekgafni.message.AckMessage;
import nl.tudelft.distributed.afekgafni.message.CandidateMessage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Ordinary extends AbstractProcess<CandidateMessage> {

	private static final long serialVersionUID = -5756267872160895568L;

	public Ordinary(int nodeId) throws RemoteException {
		super(nodeId);
	}

	private final List<CandidateMessage> candidateMessages = new ArrayList<>();
	private boolean initialized = false;

	@Override
	public void receive(CandidateMessage msg) throws RemoteException {
		log("Received message: " + msg);
		if (!initialized) {
			initialized = true;
			candidateMessages.add(msg);
			log("Starting thread ordinary process thread");
			new Thread(new OrdinaryProcess()).start();
		} else {
			synchronized (candidateMessages) {
				candidateMessages.add(msg);
				candidateMessages.notify();
			}
		}
		try { //make sure the ack is sent before returning
			synchronized (candidateMessages) {
				candidateMessages.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log("Done waiting");
	}

	public static String getRemote(int nodeId) {
		return getRemote(Ordinary.class.getName(), nodeId);
	}

	private class OrdinaryProcess implements Runnable {
		String link = null;
		int level = -1;
		int id = -1;

		@Override
		public void run() {
			while (true) {
				synchronized (candidateMessages) {
					if (link != null) {
						IProcess<AckMessage> process;
						try {
							process = (IProcess<AckMessage>) Naming.lookup(link);
						} catch (MalformedURLException | RemoteException | NotBoundException e) {
							e.printStackTrace();
							return;
						}
						try {
							process.receive(new AckMessage());
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						candidateMessages.notify();
						try { //wait for more messages being sent
							candidateMessages.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					if (!candidateMessages.isEmpty()) {
						log("Found at least one message");
						CandidateMessage maxMsg = candidateMessages.stream().max((a, b) -> a.level > b.level ? 1 : a.level == b.level ? a.nodeId > b.nodeId ? 1 : -1 : -1).get();
						log("Picked " + maxMsg);
						log("Comparing " + maxMsg.level + " > " + level + " && " + maxMsg.nodeId + " > " + id);
						if (maxMsg.level > level || (maxMsg.level == level && maxMsg.nodeId > id)) {
							log("Yes, this is a new high");
							this.level = maxMsg.level;
							this.id = maxMsg.nodeId;
							this.link = maxMsg.link;
						} else {
							log("Nope, candidate message failed. Resetting link");
							candidateMessages.notify();
							link = null;
						}
						log("Increasing level to "+ (level + 1));
						level++;
						candidateMessages.clear();
					}
				}
			}
		}
	}
}
