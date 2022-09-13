package incapsulatedCommands;

import classesOfCollection.StudyGroup;
import myCollection.MyCollection;
import user.User;
import utils.CollectionManager;
import utils.Command;

import java.io.Serializable;


public class Add implements Command, Serializable {
    private CollectionManager collectionManager;
    private StudyGroup studyGroup;
    private User user;
    private static final long serialVersionUID =952543863L;

    public Add(CollectionManager collectionManager, StudyGroup studyGroup) {
        this.collectionManager = collectionManager;
        this.studyGroup = studyGroup;
    }
    @Override
    public String execute(MyCollection myCollection) {
       return collectionManager.add(myCollection,studyGroup);
    }

    @Override
    public String getDescription() {
        return "Add";
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
