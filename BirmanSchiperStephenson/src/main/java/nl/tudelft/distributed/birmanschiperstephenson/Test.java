package nl.tudelft.distributed.birmanschiperstephenson;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class Test {
    public static final int INSTANCES = 10;
    public static final int ROUNDS = 10;

    public static void main(String[] args) {

    	if(args.length != 2){
    		System.err.println("usage: [own_ip|remote_ip]");
    		return;
    	}
    	
    	String ownIp = args[0];
    	String otherIp = args[1];
    	
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            LocateRegistry.createRegistry(1337);
        } catch (RemoteException e) {
            System.err.println("Could not create registry...: " + e);
        }

        String[] remotes = new String[INSTANCES * 2];
        for (int i = 0; i < INSTANCES; i++) {
            remotes[i] = "rmi://" + ownIp + ":1337/" + DefaultEndpointBuffer.class.getName() + "_" + i;
        }
        
        for(int i = INSTANCES; i < remotes.length ; i++){
        	remotes[i] = String.format("rmi://%s:1337/%s", otherIp, DefaultEndpointBuffer.class.getName() + "_" + i);
        }

        Thread[] threads = new Thread[INSTANCES];
        for (int i = 0; i < INSTANCES; i++) {
            try {
                InOrderEndpoint endpoint = new InOrderEndpoint(new TrollEndpoint(i, ROUNDS, remotes), ROUNDS);
                //RandomDelaySenderEndpoint endpoint = new RandomDelaySenderEndpoint(i, remotes, ROUNDS);
            	
            	Naming.bind(remotes[i], new DefaultEndpointBuffer(endpoint));
                Thread endpointThread = new Thread(endpoint);
                threads[i] = endpointThread;
            } catch (RemoteException | AlreadyBoundException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
        
        new Scanner(System.in).nextLine();
        

        for (Thread endpointThread : threads) {
            endpointThread.start();
        }

        for (Thread endpointThread : threads) {
            try {
                endpointThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("done");

        System.exit(0);
    }
}
