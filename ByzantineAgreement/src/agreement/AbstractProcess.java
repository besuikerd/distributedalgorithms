package agreement;

import agreement.messages.IMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class AbstractProcess<A extends IMessage> extends UnicastRemoteObject implements IProcess<A> {
    private static final long serialVersionUID = 1L;

    protected List<IProcess<A>> processes;
    protected int nodeId;


    private Map<Integer, Map<Class<? extends A>, List<A>>> roundBuffers;
//    private Map<Class<? extends A>, List<A>> buffer;

    public AbstractProcess(List<IProcess<A>> processes, int nodeId) throws RemoteException {
        super();
        this.nodeId = nodeId;
        this.processes = processes;
        this.roundBuffers = new HashMap<>();
    }



    protected <B extends A> void broadcast(B msg){
        for(int i = 0 ; i < processes.size() ; i++){
            try {
                processes.get(i).receive(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void receive(A msg) throws RemoteException {

        Map<Class<? extends A>, List<A>> buffer;
        synchronized (roundBuffers){
            buffer = roundBuffers.get(msg.getRound());
            if(buffer == null) {
                buffer = new HashMap<>();
                roundBuffers.put(msg.getRound(), buffer);
            }
            roundBuffers.notifyAll();
        }

        synchronized (buffer){
            List<A> list;
            if((list = buffer.get(msg.getClass())) == null){
                list = new ArrayList<>();
                buffer.put((Class<? extends A>) msg.getClass(), list);
            }
            list.add(msg);
            buffer.notify();
        }
    }

    protected <B extends A> List<B> await(Class<? extends B> cls, int r, int n){
        if(n == 0){
            return new ArrayList<>();
        }

        Map<Class<? extends A>, List<A>> buffer = null;
        synchronized (roundBuffers) {
            while(buffer == null){
                buffer = roundBuffers.get(r);
                try {
                    roundBuffers.wait();
                } catch (InterruptedException e) {
                }
            }
        }

        while(true) {
            synchronized (buffer) {
                if (buffer.containsKey(cls)){
                    List<B> list = (List<B>) buffer.get(cls);
                    if(list.size() >= n){
                        buffer.remove(cls);
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
