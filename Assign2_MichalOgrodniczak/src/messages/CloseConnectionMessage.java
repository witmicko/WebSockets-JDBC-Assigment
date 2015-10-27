package messages;

/**
 * Created by Michal Ogrodniczak on 26/10/2015.
 * Message sent when client is closing connection (on exit)
 */
public class CloseConnectionMessage extends TextMessage{

    public CloseConnectionMessage() {
        super("FIN");
    }


    @Override
    public String toString() {
        return "CloseConnectionMessage{message=" + super.getMessage()+"}";
    }
}
