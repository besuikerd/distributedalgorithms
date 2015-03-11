package nl.tudelft.distributed.afekgafni.process;

import java.rmi.RemoteException;

public class Component {
	private Candidate candidate = null;
	private Ordinary ordinary = null;

	private int nodeId;
	private String[] remotes;

	public Component(int nodeId, String[] remotes) {
		this.nodeId = nodeId;
		this.remotes = remotes;

		try {
			ordinary = new Ordinary(nodeId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void spawnCandidate() {
		if (null != candidate) {
			return;
		}

		try {
			candidate = new Candidate(nodeId, remotes);
			candidate.startElection();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
