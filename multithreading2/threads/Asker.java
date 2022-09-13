package multithreading2.threads;

import dbconnection.DBManager;
import multithreading2.AskAndProcess;
import multithreading2.Server;
import user.User;
import user.UserStatus;
import utils.Command;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Asker extends Thread {


    private ServerSocket server;


    public Asker(ServerSocket server){
        this.server = server;
    }

    public void run(){
        try {
            //System.out.println(Thread.currentThread().getName());
            Socket socket = server.accept();

            //InputStream is1 = socket.getInputStream();
            //InputStream is = socket.getInputStream();
            //ObjectInputStream ois = new ObjectInputStream(is1);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());


            UserStatus userStatus;
            do {
                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);

                User user = (User) ois.readObject();
                DBManager.setUser(user);

                userStatus = DBManager.verdict();
                dos.writeUTF(userStatus.getDescription());
            } while(UserStatus.WRONGPASSWORD.equals(userStatus));





            while(!socket.isClosed()) {
                InputStream is2 = socket.getInputStream();
                Command request = (Command) new ObjectInputStream(is2).readObject();

                Server.queue1.add(new AskAndProcess(request, socket));
                Thread.sleep(1000);
            }
        } catch (Exception e) {

        }
    }





}
