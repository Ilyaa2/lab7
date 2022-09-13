package utils;

import myCollection.MyCollection;
import user.User;

public interface Command {
    String execute(MyCollection myCollection);
    String getDescription();
    User getUser();
    void setUser(User user);
}


