package incapsulatedCommands;

import myCollection.MyCollection;
import user.User;
import utils.CollectionManager;
import utils.Command;

import java.io.Serializable;

public class Clear implements Command, Serializable {
    private CollectionManager collectionManager;
    private User user;
    private static final long serialVersionUID =9525092232563L;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(MyCollection myCollection) {
        return collectionManager.clear(myCollection);
    }
    @Override
    public String getDescription() {
        return "Clear";
    }

    @Override
    public User getUser(){
        return user;
    }

    @Override
    public void setUser(User user){
        this.user = user;
    }
}
