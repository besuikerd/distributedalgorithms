import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class RMIRegistry {
	public static void main(String[] args) throws RemoteException, InterruptedException {
		LocateRegistry.createRegistry(1337);
		synchronized(RMIRegistry.class){
			RMIRegistry.class.wait();
		}
	}
}
