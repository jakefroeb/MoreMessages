/**
 * Created by jakefroeb on 9/21/16.
 */
public class Message {
    String messageText;

    public Message(){}

    public Message(String messageText) {
        this.messageText = messageText;
    }


    @Override
    public String toString(){
        return String.format("%s \n",messageText );
    }
}
