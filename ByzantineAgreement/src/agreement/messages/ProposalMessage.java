package agreement.messages;

public class ProposalMessage implements IMessage {

	public int round;
	public Boolean w;

	public ProposalMessage(int round, Boolean w) {
		this.round = round;
		this.w = w;
	}
}
