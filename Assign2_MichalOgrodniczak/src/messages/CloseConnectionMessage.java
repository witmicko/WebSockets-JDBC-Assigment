package messages;

/**
 * Created by witmi on 26/10/2015.
 */
public class CloseConnectionMessage extends TextMessage{

    public CloseConnectionMessage() {
        super("FIN");
    }
}