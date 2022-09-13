package user;

import java.io.Serializable;
import java.util.Random;

public class User implements Serializable {
    private String login;
    private String password;
    public final static String PEPER;

    private static final long serialVersionUID = 657984L;

    static{
        PEPER = "Ol73-!.f;+eWq$";
    }


    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public static String generateRandomSalt(){
        int leftLimit = 33;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
