package singlethreading;

import dbconnection.DBConnection;
import dbconnection.DBManager;
import myCollection.MyCollection;
import user.User;
import utils.Command;
import utils.DBLoader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class SingleServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        DBConnection.makeStructure();
        MyCollection myCollection = DBLoader.loadMyCollection();
        Socket sock; ServerSocket serv;
        OutputStream os; InputStream is;
        int port = 8888;
        serv = new ServerSocket(port);
        sock = serv.accept();
        is = sock.getInputStream();
        os=sock.getOutputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        User user = (User) ois.readObject();
        DBManager.setUser(user);
        DBManager.verdict();
        while(true) {
            Command request = (Command) new ObjectInputStream(is).readObject();
            if("Exit".equals(request.getDescription())){
                String responce = request.execute(myCollection);
                DataOutputStream dis = new DataOutputStream(os);
                dis.writeUTF(responce);
                sock.close();
            } else {
                String respon= request.execute(myCollection);
                DataOutputStream dis = new DataOutputStream(os);
                dis.writeUTF(respon);
            }
        }

    }
}
