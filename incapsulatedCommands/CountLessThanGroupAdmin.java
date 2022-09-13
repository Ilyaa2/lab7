package incapsulatedCommands;

import classesOfCollection.Person;
import myCollection.MyCollection;
import user.User;
import utils.CollectionManager;
import utils.Command;

import java.io.Serializable;

public class CountLessThanGroupAdmin implements Command, Serializable {
    private CollectionManager collectionManager;
    private Person groupAdmin;
    private User user;
    private static final long serialVersionUID =4233L;

    public CountLessThanGroupAdmin(CollectionManager collectionManager, Person groupAdmin) {
        this.collectionManager = collectionManager;
        this.groupAdmin = groupAdmin;
    }

    public String execute(MyCollection myCollection) {
        return collectionManager.countLessThanGroupAdmin(myCollection,groupAdmin);
    }
    @Override
    public String getDescription() {
        return "CountLessThanGroupAdmin";
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
