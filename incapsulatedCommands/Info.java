package incapsulatedCommands;

import myCollection.MyCollection;
import user.User;
import utils.CollectionManager;
import utils.Command;

import java.io.Serializable;

public class Info implements Command, Serializable {
    private User user;
    private CollectionManager collectionManager;
    private static final long serialVersionUID =7777777777L;

    public Info(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public String execute(MyCollection myCollection) {
       return  collectionManager.info(myCollection);
    }
    @Override
    public String getDescription() {
        return "Info";
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
