import java.util.ArrayList;

/**
 * Created by jakefroeb on 9/21/16.
 */
public class User {
    String userName;
    String password;
    ArrayList<Message> messages = new ArrayList();

    public User(){}

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


}
