package user;

public enum UserStatus {
    AUTHORIZED("Authorisation successfully"),
    WRONGPASSWORD("Try again, wrong password"),
    NOTREGISTERED("You have just registered");

    private String description;

    UserStatus(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
}
