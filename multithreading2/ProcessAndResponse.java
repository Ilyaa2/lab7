package multithreading2;

import java.net.Socket;

public class ProcessAndResponse {
    private String response;
    private Socket user;
    private boolean isExit;

    public ProcessAndResponse(String response, Socket user, boolean isExit){
        this.response = response;
        this.user = user;
        this.isExit = isExit;
    }

    public String getResponse() {
        return response;
    }

    public Socket getUser() {
        return user;
    }

    public boolean isExit() {
        return isExit;
    }
}
