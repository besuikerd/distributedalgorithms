package nl.tudelft.distributed.afekgafni.process;

import nl.tudelft.distributed.afekgafni.message.AckMessage;
import nl.tudelft.distributed.afekgafni.message.CandidateMessage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Candidate extends AbstractProcess<AckMessage> {

	private static final long serialVersionUID = 7632450835597789028L;
	private String[] remotes;

	public Candidate(int nodeId, String[] remotes) throws RemoteException {
		super(nodeId);
		this.remotes = remotes;
	}

	private final List<AckMessage> acknowledgements = new ArrayList<>();


	public void startElection() {
		// Having a linked list is easier later on
		LinkedList<String> remotesCopy = new LinkedList<>();
		Collections.addAll(remotesCopy, remotes);

		int level = -1;
		int subsetSize = 0;

		while (true) {
			level += 1;
			log("At level " + level);
			if (level % 2 == 0) {
				log("Sending out messages...");
				if (remotesCopy.size() == 0) {
					// elected
					elected();
					return;
				}
				subsetSize = (int) Math.min(Math.pow(2, level / 2), remotesCopy.size());
				for (int i = subsetSize; i > 0; i--) {
					String first = remotesCopy.pop();
					try {
						String rmiString = Ordinary.getRemote(Integer.parseInt(first));
						log("Trying to lookup " + rmiString);
						Object o = Naming.lookup(rmiString);
						IProcess<CandidateMessage> that = (IProcess<CandidateMessage>) o;
						that.receive(new CandidateMessage(level, nodeId, getRemote(nodeId)));
						log("Executed receive function");
					} catch (NotBoundException | MalformedURLException | RemoteException e) {
						e.printStackTrace();
						return;
					}
				}
			} else {
				synchronized (acknowledgements) {
					if (acknowledgements.size() < subsetSize) {
						notElected();
						return;
					}
				}
			}
		}
	}

	@Override
	public void receive(AckMessage msg) throws RemoteException {
		log("received: " + msg);

		synchronized (acknowledgements) {
			acknowledgements.add(msg);
		}
	}

	public static String getRemote(int nodeId) {
		return getRemote(Candidate.class.getName(), nodeId);
	}

	/**
	 * Called when elected
	 */
	public void elected() {
		log("---------------ME SO HAPPY; I AM ALLOWED TO DO STUFF!---------------");
	}

	public void notElected() {
		log("---------------Nope, I suck---------------");
	}
}
