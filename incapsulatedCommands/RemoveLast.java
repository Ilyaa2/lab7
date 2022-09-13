package incapsulatedCommands;

import myCollection.MyCollection;
import user.User;
import utils.CollectionManager;
import utils.Command;

import java.io.Serializable;

public class RemoveLast implements Command, Serializable {
    private User user;
    private CollectionManager collectionManager;
    private static final long serialVersionUID =93L;

    public RemoveLast(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(MyCollection myCollection) {
        return collectionManager.removeLast(myCollection);
    }
    @Override
    public String getDescription() {
        return "RemoveLast";
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
