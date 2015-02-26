package nl.tudelft.distributed.birmanschiperstephenson;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class Test {
  public static final int INSTANCES = 1;
  public static final int ROUNDS = 1;

    public static void main(String[] args) {

      if (args.length != 3) {
        System.err.println("usage: [own_ip|remote_ip|mode]");
        return;
    	}
    	
    	String ownIp = args[0];
    	String otherIp = args[1];
      String mode = args[2];

      if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
          LocateRegistry.createRegistry(40001);
        } catch (RemoteException e) {
            System.err.println("Could not create registry...: " + e);
          System.exit(1);
        }

        String[] remotes = new String[INSTANCES * 2];

      int ownStart = 0;
      int ownEnd = INSTANCES;
      int otherStart = INSTANCES;
      int otherEnd = remotes.length;

      if ("slave".equals(mode)) {
        ownStart = INSTANCES;
        ownEnd = remotes.length;
        otherStart = 0;
        otherEnd = INSTANCES;
      }

      for (int i = ownStart; i < ownEnd; i++) {
        remotes[i] = "rmi://" + ownIp + ":40001/" + DefaultEndpointBuffer.class.getName() + "_" + i;
        }

      for (int i = otherStart; i < otherEnd; i++) {
        remotes[i] = String.format("rmi://%s:40001/%s", otherIp, DefaultEndpointBuffer.class.getName() + "_" + i);
        }

        Thread[] threads = new Thread[INSTANCES];
      for (int i = ownStart; i < ownEnd; i++) {
            try {
              InOrderEndpoint endpoint = new InOrderEndpoint(new TrollEndpoint(i, ROUNDS, remotes), ROUNDS, remotes.length);
                //RandomDelaySenderEndpoint endpoint = new RandomDelaySenderEndpoint(i, remotes, ROUNDS);

              Naming.bind(remotes[i], new DefaultEndpointBuffer(endpoint));
                Thread endpointThread = new Thread(endpoint);
              threads[i - ownStart] = endpointThread;
            } catch (RemoteException | AlreadyBoundException | MalformedURLException e) {
                e.printStackTrace();
            }
        }
        
        new Scanner(System.in).nextLine();
      System.out.println("Starting");

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

//        System.exit(0);
    }
}
