package messages;

/**
 * Created by Michal Ogrodniczak on 26/10/2015.
 * Simple message containing text message.
 */
public class TextMessage extends Message {
    private String message;


    public TextMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "TextMessage{" +
                "message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String prettyPrint(){
        return message + "\n";
    }
}
