package agreement.messages;

public class NotificationMessage implements IMessage {

	public int round;
	public boolean v;

	public NotificationMessage(int round, boolean v) {
		this.round = round;
		this.v = v;
	}
}
