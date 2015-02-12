package nl.tudelft.distributed.birmanschiperstephenson;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Test {
    private final static int INSTANCES = 10;

    public static void main(String[] args) {
        try {
			if(System.getSecurityManager() == null){
                System.setSecurityManager(new SecurityManager());
            }
			Registry registry = LocateRegistry.createRegistry(1337);
			
			Registry remoteRegistry = LocateRegistry.getRegistry("localhost", 1337);

            String[] remotes = new String[INSTANCES];
            //String[] names = new String[INSTANCES];
            for (int i = 0; i < INSTANCES; i++) {
                //names[i] = DefaultEndpointBuffer.class.getName() +"_"+ i;
                remotes[i] = "rmi://localhost:1337/" + DefaultEndpointBuffer.class.getName() + "_" + i;
            }

            for (int i = 0; i < INSTANCES; i++) {
                try {
                    Naming.bind(remotes[i], new DefaultEndpointBuffer(new RandomDelaySenderEndpoint(0, remotes, 0)));
                } catch (AlreadyBoundException | MalformedURLException e) {
                    System.err.println("YOU SUCK AT CONSTRUCTING URLS: " + e);
                }
            }
            //IEndpointBuffer endpointBuffer = (IEndpointBuffer) remoteRegistry.lookup(remotes[1]);

            try {
                IEndpointBuffer endpointBuffer = (IEndpointBuffer) Naming.lookup(remotes[1]);
                endpointBuffer.receive("Blabalbal", 0, new int[0]);
            } catch (MalformedURLException e) {
                System.err.println("YOU SUCK AT CONSTRUCTING URLS: " + e);
            }

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
		}
	}
}
