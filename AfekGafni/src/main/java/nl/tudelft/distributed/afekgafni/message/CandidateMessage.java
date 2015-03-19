package nl.tudelft.distributed.afekgafni.message;

import java.io.Serializable;

public class CandidateMessage implements Serializable {
	private static final long serialVersionUID = 3299617204307238780L;

	public final int level;
	public final int nodeId;
	public final String link;

	public CandidateMessage(int level, int nodeId, String link) {
		this.level = level;
		this.nodeId = nodeId;
		this.link = link;
	}

	@Override
	public String toString() {
		return String.format("CandidateMessage(%s, %s)", level, nodeId);
	}
}
