package multithreading2;

import utils.Command;

import java.net.Socket;

public class AskAndProcess {
    private Command task;
    private Socket socketUser;

    public AskAndProcess(Command task, Socket socketUser){
        this.task = task;
        this.socketUser = socketUser;
    }

    public Command getTask(){
        return task;
    }
    public Socket getSocketUser(){
        return socketUser;
    }
}
