package incapsulatedCommands;

import myCollection.MyCollection;
import user.User;
import utils.CollectionManager;
import utils.Command;

import java.io.Serializable;

public class FilterContainsName implements Command, Serializable {
    private User user;
    private String name;
    private CollectionManager collectionManager;
    private static final long serialVersionUID =92222222263L;

    public FilterContainsName(CollectionManager collectionManager, String name) {
        this.collectionManager = collectionManager;
        this.name = name;
    }

    public String execute(MyCollection myCollection) {
        return collectionManager.filterContainsName(myCollection,name);
    }
    @Override
    public String getDescription() {
        return "FilterContainsName";
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
