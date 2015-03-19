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
	public void receive(CandidateMessage msg) {
		if(!initialized){
			this.candidateMessages = new ArrayList<CandidateMessage>();
			candidateMessages.add(msg);
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
					Candidate candidate = null;
					try {
						candidate = (Candidate) Naming.lookup(link);
					} catch (MalformedURLException | RemoteException| NotBoundException e) {
						e.printStackTrace();
						return;
					}
					candidate.receive(new AckMessage());
					synchronized(candidateMessages){
						candidateMessages.notify();
					}
				}
				level++;
				synchronized(candidateMessages){
					if(!candidateMessages.isEmpty()){
						CandidateMessage maxMsg = candidateMessages.stream().max((a, b) -> a.toString().compareTo(b.toString())).get();
						if(maxMsg.level > level && maxMsg.nodeId > nodeId){
							this.level = maxMsg.level;
							Ordinary.this.nodeId = maxMsg.nodeId;
							this.link = maxMsg.link;
						} else{
							link = null;
						}
					}
					try { //wait for more messages being sent
						candidateMessages.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
