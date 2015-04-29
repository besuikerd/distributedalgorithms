package agreement;

import agreement.messages.IMessage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ByzantineAgreementTest {
    public static final String HOST = "rmi://localhost:1337/";

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
        try {
            if (args.length == 5) {
                int arg = 0;
                int nodeId = Integer.parseInt(args[arg++]);
                int numberOfNodes = Integer.parseInt(args[arg++]);
                boolean initialOpinion = Integer.parseInt(args[arg++]) == 1;
                int tolerance = Integer.parseInt(args[arg++]);
                ProcessBehaviour processBehaviour = null;
                switch(args[arg++].toLowerCase()){
                    case "jerk":
                        processBehaviour = ProcessBehaviour.JERK;
                        break;
                    case "lazy":
                        processBehaviour = processBehaviour.LAZY;
                        break;
                    default:
                        processBehaviour = processBehaviour.NICE;
                }
                List<IProcess<IMessage>> nodes = new ArrayList<>();
                RandomizedByzantine byzantine = new RandomizedByzantine(nodes, initialOpinion, nodeId, tolerance, processBehaviour);
                Naming.rebind(HOST + nodeId, byzantine);
                Thread.sleep(5000);
                for(int i = 0 ; i < numberOfNodes ; i++){
                    if(i != nodeId) {
                        IProcess<IMessage> process = (IProcess<IMessage>) Naming.lookup(HOST + i);
                        nodes.add(process);
                    } else{
                        nodes.add(byzantine);
                    }
                }

                new Thread(byzantine).start();
            } else {
                System.out.println("usage nodeID #nodes initialValue faultTolerance");
            }
        } catch(NumberFormatException e){
            System.out.println("You failed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
