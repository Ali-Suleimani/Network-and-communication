import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import tcpclient.TCPClient;



public class ConcHTTPAsk {
    Integer timeout = null;
    Integer port = null;
    Integer limit = null;
    boolean shutdown = false;
    String hostname = null;
    String string = null;


    public static void main(String[] args) throws Exception {
        int port = 0;
        
        System.out.println(" This is the main thread" + Thread.currentThread().getName());
        
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        try{

        
            while (true){
                Socket clientSocket = serverSocket.accept();
                MyRunnable r1 = new MyRunnable(clientSocket);
                Thread t1 = new Thread(r1);
                t1.start();
            }
        }

        catch (Exception e){
            System.out.println("catch if something went wrong");
        }

        //MyRunnable runnable = new MyRunnable(port);
        //Runnable t1 = new MyRunnable(ServerSocket.accept(port));
        //new Thread(t1).start();
        
        //MyRunnable t1 = new MyRunnable(ServerSocket.accept);
        //t1.start();


    }

}    

