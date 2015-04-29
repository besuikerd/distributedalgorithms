package agreement;

import agreement.messages.IMessage;
import agreement.messages.NotificationMessage;
import agreement.messages.ProposalMessage;

public class RandomizedByzantine {
	public int n = 0;		// Number of processes
	public int f = 0;		// Max allowed faulty processes

	public boolean doWork(boolean v) {
		int r = 1;
		boolean decided = false;
		while (true) {
			broadcast(new NotificationMessage(r, v));
			// TODO: wait for  n-f NotificationMessage(r, *)
			int received0 = 0;
			int received1 = 0;
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
				return v;
			} else {
				// TODO: wait for n-f ProposalMessage(r, *)
			}

			received0 = 0;
			received1 = 0;
			int receivedUnknown = 0;
			if ((received0 + received1) > f){
				majorityReceived = Math.max(received0, received1);
				// find what was actually received
				v = received1 == majorityReceived;
				if (majorityReceived > 3 * f){
					decided = true;
				}
			} else {
				v = Math.round(Math.random()) == 1;
			}
			r++;
		}
	}

	public void broadcast(IMessage message) {

	}
}
