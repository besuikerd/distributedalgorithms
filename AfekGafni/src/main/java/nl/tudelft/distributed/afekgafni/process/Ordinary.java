package nl.tudelft.distributed.afekgafni.process;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.distributed.afekgafni.message.AckMessage;
import nl.tudelft.distributed.afekgafni.message.CandidateMessage;

public class Ordinary extends AbstractProcess<CandidateMessage> {
	
	private static final long serialVersionUID = -5756267872160895568L;

	public Ordinary(int nodeId) throws RemoteException {
		super(nodeId);
	}
	
	private List<CandidateMessage> candidateMessages;
	private boolean initialized = false;
	
	@Override
	public void receive(CandidateMessage msg) throws RemoteException {
		log("Received message: "+ msg);
		if(!initialized){
			this.candidateMessages = new ArrayList<CandidateMessage>();
			candidateMessages.add(msg);
			log("Starting thread ordinary process thread");
			new Thread(new OrdinaryProcess()).start();
		} else{
			synchronized(candidateMessages){
				candidateMessages.add(msg);
				candidateMessages.notify();

				try { //make sure the ack is sent before returning
					candidateMessages.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getRemote(int nodeId) {
		return getRemote(Ordinary.class.getName(), nodeId);
	}
	
	private class OrdinaryProcess implements Runnable{
		String link = null;
		int level = -1;
		
		@Override
		public void run() {
			while(true){
				if(link != null){
					log("No link yet");
					Candidate candidate = null;
					try {
						candidate = (Candidate) Naming.lookup(link);
					} catch (MalformedURLException | RemoteException| NotBoundException e) {
						e.printStackTrace();
						return;
					}
					log("Sending Ack to " + link);
					try {
						candidate.receive(new AckMessage());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					synchronized(candidateMessages){
						candidateMessages.notify();
					}
				}
				synchronized(candidateMessages){
					log("Checking messages");
					if(!candidateMessages.isEmpty()){
						log("Found at least one message");
						CandidateMessage maxMsg = candidateMessages.stream().max((a, b) -> a.toString().compareTo(b.toString())).get();
						log("Picked "+ maxMsg);
						log("Comparing "+ maxMsg.level +" > "+ level +" && "+ maxMsg.nodeId +" > "+ nodeId);
						if(maxMsg.level > level || (maxMsg.level == level && maxMsg.nodeId > nodeId)) {
							log("Yes, this is a new high");
							this.level = maxMsg.level;
							Ordinary.this.nodeId = maxMsg.nodeId;
							this.link = maxMsg.link;
						} else{
							log("Nope, candidate message failed. Resetting link");
							link = null;
						}
					}
					try { //wait for more messages being sent
						candidateMessages.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				level++;
			}
		}
	}
}
