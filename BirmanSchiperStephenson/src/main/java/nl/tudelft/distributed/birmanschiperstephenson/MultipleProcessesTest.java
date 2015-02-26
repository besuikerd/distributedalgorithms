package nl.tudelft.distributed.birmanschiperstephenson;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MultipleProcessesTest {
  public static void main(String[] args) {

    if (args.length < 2) {
      System.err.println("usage: [ROUNDS|own|remotes...]");
      return;
    }

    int ROUNDS = Integer.parseInt(args[0]);

    String[] args2 = new String[args.length - 1];
    System.arraycopy(args, 1, args2, 0, args2.length);

    args = args2;

    String own = args[0];
    int ownId = Integer.parseInt(own);

    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }

    Registry registry;
    try {
      if (ownId == 0) {
        registry = LocateRegistry.createRegistry(40001);
      }
    } catch (RemoteException e) {
      System.err.println("Could not create registry...: " + e);
      System.exit(1);
    }

    String[] remotes = new String[args.length];

    for (int i = 0; i < args.length; i++) {
      remotes[i] = String.format("rmi://%s:40001/%s", "localhost", i);
    }

    //System.out.println(Arrays.toString(remotes));

    Thread endpointThread = null;
    try {
      InOrderEndpoint endpoint = new InOrderEndpoint(new TrollEndpoint(ownId, ROUNDS, remotes), ROUNDS, remotes.length);
      //RandomDelaySenderEndpoint endpoint = new RandomDelaySenderEndpoint(i, remotes, ROUNDS);

      System.out.println("Binding node " + ownId);
      Naming.bind(remotes[ownId], new DefaultEndpointBuffer(endpoint));
      endpointThread = new Thread(endpoint);
    } catch (RemoteException | AlreadyBoundException | MalformedURLException e) {
      e.printStackTrace();
    }

    //new Scanner(System.in).nextLine();
    //System.out.println("Starting");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    endpointThread.start();

    try {
      endpointThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    //System.out.println("done");

//        System.exit(0);
  }
}
