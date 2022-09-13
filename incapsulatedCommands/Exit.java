package incapsulatedCommands;

import myCollection.MyCollection;
import user.User;
import utils.CollectionManager;
import utils.Command;

import java.io.Serializable;

public class Exit implements Command, Serializable {
    private User user;
    private CollectionManager collectionManager;
    private static final long serialVersionUID =95432356943863L;

    public Exit(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(MyCollection myCollection) {
        return collectionManager.exit(myCollection);
    }

    @Override
    public String getDescription() {
        return "Exit";
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
