package messages;

import java.io.*;

/**
 * Created by Michal Ogrodniczak on 26/10/2015.
 * super class, handles serialization
 */
public class Message implements java.io.Serializable{


    /**
     * default constructor
     */
    public Message(){}


    /**
     * Java serializable method - converts byte array into object of this class
     * @param bytes - byte array to be converted into an object of this class
     * @return object of this class
     */
    public static Message convertFromBytes(byte[] bytes) throws ClassNotFoundException {
        //java 1.7 with resources
        Message message = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            message = (Message) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;

        //older messy method
//        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//        ObjectInput in = null;
//        messages.Message message = null;
//        try {
//            in = new ObjectInputStream(bis);
//            Object o = in.readObject();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                bis.close();
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//        }
//        return message;
    }


    /**
     * Java serializable method - converts an object into a byte array
     * @return byte array of this object
     */
    public byte[] convertToBytes(){
        //java 1.7 with resources
        byte [] bytes = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(this);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;

        //or older, messy way
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutput out = null;
//        byte [] bytes = null;
//
//        try {
//            out = new ObjectOutputStream(bos);
//            out.writeObject(this);
//            bytes = bos.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//            try {
//                bos.close();
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//        }
//        return bytes;
    }


    public String prettyPrint() {
        return null;
    }
}
