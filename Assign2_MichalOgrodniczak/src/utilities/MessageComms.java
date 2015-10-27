package utilities;

import messages.Message;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by witmi on 27/10/2015.
 */
public class MessageComms {


    /**
     * reads in bytes from the input stream and returns a messages.Message object
     * @return
     */
    public static Message readInMessage(DataInputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Message message = null;

        try {
            while (inputStream.available() > 0) {
                int i = inputStream.read();
                if (i > -1) {
                    baos.write(i);
                }
            }

            message = Message.convertFromBytes(baos.toByteArray());

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return message;
    }
}
