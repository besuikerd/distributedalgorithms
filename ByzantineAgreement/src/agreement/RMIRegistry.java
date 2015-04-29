package agreement;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RMIRegistry {
	private static Registry createRegistry() {
		try {
			return LocateRegistry.createRegistry(1337);
		} catch (RemoteException e) {
			System.exit(1);
			return null;
		}
	}

	public static final Registry reg = createRegistry();

	public static void main(String[] args) throws RemoteException, InterruptedException {
		synchronized (reg) {
			reg.wait();
		}
	}
}
