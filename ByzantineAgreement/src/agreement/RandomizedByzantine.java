package agreement;

import agreement.messages.IMessage;
import agreement.messages.NotificationMessage;
import agreement.messages.ProposalMessage;

import java.rmi.RemoteException;
import java.util.List;

public class RandomizedByzantine extends AbstractProcess<IMessage> implements Runnable {
	public int faultTolerance = 0;        // Max allowed faulty processes
	public boolean opinion;
	protected ProcessBehaviour behaviour;

	public RandomizedByzantine(List<IProcess<IMessage>> iProcesses, boolean opinion, int nodeId, int faultTolerance, ProcessBehaviour behaviour) throws RemoteException {
		super(iProcesses, nodeId);
		log("Starting with " + opinion + " in " + behaviour.name() + "-mode");
		this.faultTolerance = faultTolerance;
		this.opinion = opinion;
		this.behaviour = behaviour;
	}

	@Override
	public void run() {
		doWork(opinion, faultTolerance);
	}

	public void log(String message) {
		System.out.println("[" + nodeId + "] " + message);
	}

	public boolean doWork(boolean v, int f) {
		int n = processes.size();
		int r = 1;
		boolean decided = false;
		while (true) {
			broadcast(new NotificationMessage(r, v));

			List<NotificationMessage> notificationMessages = await(NotificationMessage.class, n - f);
			int received1 = (int) notificationMessages.stream().filter(x -> x.v).count();
			int received0 = notificationMessages.size() - received1;

			/*log("received true: " + received1);
			log("received false: " + received0);*/

			int majorityReceived;
			if ((received0 + received1) > (n + f) / 2) {
				majorityReceived = Math.max(received0, received1);
				// find what was actually received
				boolean receivedMessage = received1 == majorityReceived;
				broadcast(new ProposalMessage(r, receivedMessage));
			} else {
				broadcast(new ProposalMessage(r, null));
			}
			if (decided) {
				log("decided " + v + " after " + r + " rounds");
				return v;
			}
			//log("awaiting proposal");
			List<ProposalMessage> proposalMessages = await(ProposalMessage.class, n - f);
			//log("got "+ proposalMessages.size() +" proposals");
			received0 = (int) proposalMessages.stream().filter(x -> x.w != null && !x.w).count();
			received1 = (int) proposalMessages.stream().filter(x -> x.w != null && x.w).count();

			if ((received0 + received1) > f) {
				majorityReceived = Math.max(received0, received1);
				// find what was actually received
				v = received1 == majorityReceived;
				if (majorityReceived > 3 * f) {
					decided = true;
				}
			} else {
				boolean oldV = v;
				v = Math.round(Math.random()) == 1;
				log("Switched from v = " + oldV + " to " + v);
			}
			r++;
		}
	}

	@Override
	protected <B extends IMessage> void broadcast(B msg) {
		long sleepDelay = 200;

		try {
			Thread.sleep((long) (Math.random() * sleepDelay));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		switch (behaviour) {
			case JERK:
				msg.setValue(msg.getValue() == null ? Math.round(Math.random()) == 1 : !msg.getValue());
				super.broadcast(msg);
			case LAZY:
				break;
			case NICE:
			default:
				super.broadcast(msg);
		}
		try {
			Thread.sleep((long) (Math.random() * sleepDelay));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
