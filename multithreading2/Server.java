package multithreading2;

import dbconnection.DBConnection;
import multithreading2.threads.Asker;
import multithreading2.threads.Responder;
import myCollection.MyCollection;
import utils.DBLoader;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {


    public static LinkedBlockingQueue<AskAndProcess> queue1 = new LinkedBlockingQueue<>();
    public static LinkedBlockingQueue<ProcessAndResponse> queue2 = new LinkedBlockingQueue<>();
    public volatile static ServerSocket server;


    public static void main(String[] args) throws Exception {
        DBConnection.makeStructure();
        MyCollection myCollection = DBLoader.loadMyCollection();
        int port = 8888;
        server = new ServerSocket(port);

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        Runnable runnable = () -> {
            AskAndProcess askAndProcess = null;
            do{
                //System.out.println(Thread.currentThread().getName());
                if (!queue1.isEmpty()) {
                    try {
                        askAndProcess = queue1.take();

                        String response = askAndProcess.getTask().execute(myCollection);
                        if ("Exit".equals(askAndProcess.getTask().getDescription())){
                            queue2.add(new ProcessAndResponse(response, askAndProcess.getSocketUser(),true));
                        } else{
                            queue2.add(new ProcessAndResponse(response, askAndProcess.getSocketUser(),false));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(askAndProcess == null || !askAndProcess.getSocketUser().isClosed());
        };

        //каким образом будут создаваться новые потоки одного класса?
        for (int i = 0; i < 4; i++) {


//            Asker asker = new Asker(server);
//            asker.start();


            new Asker(server).start();
            executorService.execute(runnable);
            new Responder().start();
        }

    }
}
