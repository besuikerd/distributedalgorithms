package agreement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

abstract public class AbstractProcess<A> extends UnicastRemoteObject implements IProcess<A> {
    private static final long serialVersionUID = 1L;

    protected List<IProcess<A>> processes;
    protected ProcessBehaviour behaviour;
    protected int nodeId;

    public AbstractProcess(List<IProcess<A>> processes, ProcessBehaviour behaviour, int nodeId) throws RemoteException {
        super();
        this.nodeId = nodeId;
        this.processes = processes;
    }

    protected void broadcast(A msg) throws RemoteException{
        for(int i = 0 ; i < processes.size() ; i++){
            if(i != nodeId){
                processes.get(i).receive(msg);
            }
        }
    }

    /*
    public static String getRemote(String name, int nodeId) {
        return "rmi://localhost:1337/" + name + "_" + nodeId;
    }

    public void log(String message) {
        System.out.println("[" + this.getClass().getSimpleName() + " " + nodeId + "] " + message);
    }
    */
}
