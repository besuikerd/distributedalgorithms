package nl.tudelft.distributed.afekgafni.app;

import nl.tudelft.distributed.afekgafni.process.Candidate;
import nl.tudelft.distributed.afekgafni.process.Ordinary;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class AfekNode {
	public static final String USAGE = "usage: id isCandidate otherNodes*";

	public static void main(String[] args) {
		if (args.length >= 2) {
			try {
				int nodeId = Integer.parseInt(args[0]);
				boolean isCandidate = args[1].toLowerCase().equals("candidate");
				String[] otherNodes = new String[args.length - 2];
				System.arraycopy(args, 2, otherNodes, 0, otherNodes.length);
				Thread.sleep(5000);
				start(nodeId, isCandidate, otherNodes);
			} catch (NumberFormatException e) {
				System.out.println(USAGE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(USAGE);
		}
	}

	public static void start(int nodeId, boolean isCandidate, String[] otherNodes) {
		try {
			Ordinary ordinary = new Ordinary(nodeId);
			Naming.rebind(ordinary.getRemote(), ordinary);

			if (isCandidate) {
				Candidate candidate = new Candidate(nodeId, otherNodes);
				candidate.startElection();
			}
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
