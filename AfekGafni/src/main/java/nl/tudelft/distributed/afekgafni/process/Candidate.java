package nl.tudelft.distributed.afekgafni.process;

import nl.tudelft.distributed.afekgafni.message.IMessage;
import nl.tudelft.distributed.afekgafni.message.Tuple2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;

public class Candidate extends IProcess {
	private String[] remotes;

	protected Candidate(int nodeId, String[] remotes) throws RemoteException {
		super(nodeId);
		this.remotes = remotes;
	}

	public void startElection() {
		// Having a linked list is easier later on
		LinkedList<String> remotesCopy = new LinkedList<>();

		for (String remote : remotes) {
			remotesCopy.addFirst(remote);
		}

		int level = -1;
		int acksReceived = 0;
		int subsetSize = 0;

		while (true) {
			level += 1;
			if (level % 2 == 0) {
				if (remotesCopy.size() == 0) {
					// elected
					doWork();
				}
				subsetSize = (int) Math.min(Math.pow(2, level / 2), remotesCopy.size());

				for (int i = subsetSize; i > 0; i--) {
					String first = remotesCopy.pop();

					try {
						Object o = Naming.lookup(first);
						// should be true...
						if (o instanceof Ordinary) {
							IMessage m = new Tuple2<>(level, nodeId);
							Ordinary that = (Ordinary) o;
							that.receive(m);
						}
					} catch (NotBoundException | MalformedURLException | RemoteException e) {
						e.printStackTrace();
					}

					boolean result = true;

					if (result) {
						acksReceived++;
					}
				}
			} else {
				if (acksReceived < subsetSize) {
					// NOT ELECTED
					return;
				}
			}
		}
	}

	/**
	 * Called when elected
	 */
	public void doWork() {
		System.out.println("ME SO HAPPY; I AM ALLOWED TO DO STUFF!");
	}
}
