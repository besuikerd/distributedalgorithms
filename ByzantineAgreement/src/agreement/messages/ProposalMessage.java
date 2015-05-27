package agreement.messages;

public class ProposalMessage implements IMessage {

	public int round;
	public Boolean w;

	public ProposalMessage(int round, Boolean w) {
		this.round = round;
		this.w = w;
	}

	@Override
	public Boolean getValue() {
		return w;
	}

	@Override
	public int getRound() {
		return round;
	}

	@Override
	public void setValue(Boolean newValue) {
		this.w = newValue;
	}
}
