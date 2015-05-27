package agreement.messages;

import java.io.Serializable;

public interface IMessage extends Serializable {
    Boolean getValue();
    void setValue(Boolean newValue);
    int getRound();
}
