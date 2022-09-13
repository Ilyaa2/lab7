package multithreading2.threads;

import multithreading2.ProcessAndResponse;
import multithreading2.Server;

import java.io.DataOutputStream;
import java.io.IOException;

public class Responder extends Thread{
    @Override
    public void run() {
        ProcessAndResponse par = null;
        do{
            //System.out.println(Thread.currentThread().getName());
            if (!Server.queue2.isEmpty()){
                try {
                    par = Server.queue2.take();
                    DataOutputStream dos = new DataOutputStream(par.getUser().getOutputStream());
                    dos.writeUTF(par.getResponse());
                    if (par.isExit()){
                        par.getUser().close();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(par== null || !par.getUser().isClosed());
    }
}
