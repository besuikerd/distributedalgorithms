package agreement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class AbstractProcess<A> extends UnicastRemoteObject implements IProcess<A> {
    private static final long serialVersionUID = 1L;

    protected List<IProcess<A>> processes;
    protected ProcessBehaviour behaviour;
    protected int nodeId;
    private Map<Class<? extends A>, List<A>> buffer;

    public AbstractProcess(List<IProcess<A>> processes, ProcessBehaviour behaviour, int nodeId) throws RemoteException {
        super();
        this.nodeId = nodeId;
        this.processes = processes;
        this.buffer = new HashMap<>();
    }



    protected <B extends A> void broadcast(B msg) throws RemoteException{
        for(int i = 0 ; i < processes.size() ; i++){
            if(i != nodeId){
                processes.get(i).receive(msg);
            }
        }
    }

    @Override
    public void receive(A msg) throws RemoteException {
        synchronized (buffer){
            List<A> list = null;
            if((list = buffer.get(msg.getClass())) == null){
                list = new ArrayList<>();
                buffer.put((Class<? extends A>) msg.getClass(), list);
            }
            list.add(msg);
            buffer.notify();
        }
    }

    protected <B extends A> List<B> await(Class<? extends B> cls, int n){
        while(true) {
            synchronized (buffer) {
                if (buffer.containsKey(cls)){
                    List<B> list = (List<B>) buffer.get(cls);
                    if(list.size() >= n){
                        return list;
                    } else{
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else{
                    try {
                        buffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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
