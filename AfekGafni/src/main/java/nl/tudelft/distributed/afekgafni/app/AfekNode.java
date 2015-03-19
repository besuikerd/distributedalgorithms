package nl.tudelft.distributed.afekgafni.app;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import nl.tudelft.distributed.afekgafni.process.Candidate;
import nl.tudelft.distributed.afekgafni.process.Ordinary;

public class AfekNode {
	public static final String USAGE = "id isCandidate otherNodes*";
	public static final int RMI_PORT = 1337;
	
	
	public static void main(String[] args) {
		if(args.length < 3){
			try{
				int nodeId = Integer.parseInt(args[0]);
				boolean isCandidate = args[1].toLowerCase().equals("candidate");
				String[] otherNodes = new String[args.length - 2];
				System.arraycopy(args, 2, otherNodes, 0, otherNodes.length);
				start(nodeId, isCandidate, otherNodes);
			} catch(NumberFormatException e){
				System.out.println(USAGE);
			}
			
			
			
		} else{
			System.out.println(USAGE);
		}
	}
	
	public static void start(int nodeId, boolean isCandidate, String[] otherNodes){
		try{
			Ordinary ordinary = new Ordinary(nodeId);
			Naming.bind(ordinary.getRemote(), ordinary);
			
			if(isCandidate){
				Candidate candidate = new Candidate(nodeId, otherNodes);
			}
		} catch(RemoteException | MalformedURLException | AlreadyBoundException e){
			e.printStackTrace();
			return;
		}
		
		//Naming.bind(name, obj);
		
		
		
	}
}
