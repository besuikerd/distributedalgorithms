package nl.tudelft.distributed.birmanschiperstephenson;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Test {
    private final static int INSTANCES = 50;
    private static final int ROUNDS = 1000;

    public static void main(String[] args) {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            LocateRegistry.createRegistry(1337);
        } catch (RemoteException e) {
            System.err.println("Could not create registry...: " + e);
        }

        String[] remotes = new String[INSTANCES];
        for (int i = 0; i < INSTANCES; i++) {
            remotes[i] = "rmi://localhost:1337/" + DefaultEndpointBuffer.class.getName() + "_" + i;
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
