package multithreading2;

import exceptions.*;
import incapsulatedCommands.ExecuteScript;
import user.User;
import user.UserStatus;
import utils.CollectionManager;
import utils.Command;
import utils.InteractiveCollector;
import utils.Switch;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private static int PORT = 8888;

    private static Socket socket;
    private static InputStream is;
    private static OutputStream os;

    public static void main(String[] args) {
        Command command = null;
        ArrayList<Command> commands = new ArrayList<>();
        Switch sw = new Switch(new InteractiveCollector(), new CollectionManager(), commands);
        try {
            connect();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Trying to reconnect");

            try {
                Thread.sleep(15000);
                connect();
            } catch (IOException ee) {
                System.out.println("Server is not available, try another time");
                System.exit(0);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }


        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to my console application");
        System.out.println("Please authorize or register in the system");


        String verdict = UserStatus.WRONGPASSWORD.getDescription();
        do {
            try {
                System.out.println("Enter your login");
                String f1 = scanner.nextLine();
                System.out.println("Enter your password");
                String f2 = scanner.nextLine();
                User user = new User(f1, f2);
                sendToServer(user);
                verdict = getResponse();
                System.out.println(verdict);
            } catch (NoSuchElementException e){
                System.exit(-1);
            } catch(IOException e){
                System.out.println("Please try again, server side error");
            }
        } while(UserStatus.WRONGPASSWORD.getDescription().equals(verdict));



        System.out.println("Enter any command to begin work with console. To see the list of available commands type \"help\"\n");
        while (true) {
            try {
                String s = scanner.nextLine();
                command = sw.interpret(s);
                if (command instanceof ExecuteScript) {
                    System.out.println(command.execute(null));
                    for (Command com : commands) {
                        sendToServer(com);
                        String str = getResponse();
                        System.out.println(str);
                        if ("Sorry, there's a lot of elements here, can't add more".equals(str)) break;
                        if ("Exit".equals(com.getDescription())) System.exit(0);
                    }
                } else {
                    sendToServer(command);
                    String response = getResponse();
                    System.out.println(response);
                    if (command.getDescription().equals("Exit")) System.exit(0);
                    //oos.writeObject(command);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Can not find the file");
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("Trying to reconnect");
                try {
                    Thread.sleep(15000);
                    connect();
                } catch (Exception ee) {
                    System.out.println("Server is not available, try another time");
                    System.exit(0);
                }
            }
            catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Parameter is incorrect");
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            } catch (NullArgumentException e) {
                System.out.println("Argument cannot be null");
            } catch (UnderZeroValueException e) {
                System.out.println("Parameter cannot be under zero");
            } catch (IllegalArgumentException e) {
                System.out.println("Parameter must match with allowed values in enum");
            } catch (DateTimeException e) {
                System.out.println("Something happened wrong, you probably entered incorrect parameter in date or time");
            } catch (MaxValueException e) {
                System.out.println("Too large number");
            } catch (UnknownCommandException e) {
                System.out.println("Unknown command");
            } catch (NoSuchElementException e) {
                System.out.println("end of file reached");
                break;
            } catch (Exception e) {
                System.out.println("Please try again");
            }
        }


    }


    public static void connect() throws UnknownHostException, IOException {
        socket = new Socket(InetAddress.getLocalHost(), PORT);
        if (socket.isConnected()) System.out.println("Connection established");
    }

    //IOException для каждого по разному
    public static void sendToServer(Command command) throws IOException {
        //byteBuffer = ByteBuffer.allocate(3000000);
        os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(command);
        oos.flush();

    }

    public static void sendToServer(User user){
        try {
            os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(user);
            oos.flush();
        } catch (FileNotFoundException e) {
            System.out.println("Can not find the file");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Trying to reconnect");
        } catch (NumberFormatException | InputMismatchException e) {
            System.out.println("Parameter is incorrect");
        } catch (IllegalArgumentException e) {
            System.out.println("Parameter must match with allowed values in enum");
        } catch (DateTimeException e) {
            System.out.println("Something happened wrong, you probably entered incorrect parameter in date or time");
        } catch (UnknownCommandException e) {
            System.out.println("Unknown command");
        } catch (NoSuchElementException e) {
            System.out.println("end of file reached");
        } catch (Exception e) {
            System.out.println("Please try again");
        }
    }

    public static String getResponse() throws IOException {

        is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        return dis.readUTF().trim();
    }
}
