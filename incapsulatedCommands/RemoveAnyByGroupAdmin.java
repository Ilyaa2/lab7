package incapsulatedCommands;

import classesOfCollection.Person;
import myCollection.MyCollection;
import user.User;
import utils.CollectionManager;
import utils.Command;

import java.io.Serializable;

public class RemoveAnyByGroupAdmin implements Command, Serializable {
    private User user;
    private CollectionManager collectionManager;
    private Person groupAdmin;
    private static final long serialVersionUID =99999999999L;

    public RemoveAnyByGroupAdmin(CollectionManager collectionManager, Person groupAdmin) {
        this.collectionManager = collectionManager;
        this.groupAdmin = groupAdmin;
    }

    public String execute(MyCollection myCollection) {
        return collectionManager.removeAnyByGroupAdmin(myCollection,groupAdmin);
    }
    @Override
    public String getDescription() {
        return "RemoveAnyByGroupAdmin";
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

