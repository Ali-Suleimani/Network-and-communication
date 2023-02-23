import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import tcpclient.TCPClient;
public class MyRunnable implements Runnable {
    Integer timeout = null;
    Integer port = null;
    Integer limit = null;
    boolean shutdown = false;
    String hostname = null;
    String string = null;
    Socket clientSocket;

    public MyRunnable (Socket clientSocket){
        this.clientSocket = clientSocket;
        //System.out.println("printout an integer number");


    }
       @Override
    public void run(){
        try{

            String HTTP404 = "HTTP/1.1 404 Not Found\r\n\r\n"; 
            String HTTP200 = "HTTP/1.1 200 OK\r\n\r\n";
            String HTTP400 = "HTTP/1.1 400 Bad Request\r\n\r\n";
            

            //buffer Array
            byte [] bufferSize = new byte [1024]; 
            

                // här ska vi läsa och skriva data som vi får från server
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();

                //här skriver vi tillbaka datan
                inputStream.read(bufferSize); 

                // omvandla bytes till string
                String fromInput = new String(bufferSize, StandardCharsets.UTF_8);
                System.out.println(fromInput);

                //kollan vad som finns i URL

                if(!fromInput.contains("HTTP/1.1") ||!fromInput.contains("GET")){
                    String lineFnish = "\r\n";  //marks the end of the line

                    String svar = "HTTP/1.1 400 bad request" + lineFnish + lineFnish;  // Vet inte vad den gör
                    outputStream.write(svar.getBytes());
                    clientSocket.close();
                }

                else {
                    //System.out.println(fromInput);
                    String [] resultSvar = fromInput.split("HTTP/1.1");

                      if(!resultSvar[0].contains("/ask") || !resultSvar[0].contains("hostname=") || !resultSvar[0].contains("port=")){
                       String lineFnish1 = "\r\n\r\n"; //marks the end of the line
                       String svar1 = "HTTP/1.1 404 Not Found" + lineFnish1;
                       outputStream.write(svar1.getBytes());
                       clientSocket.close();
                      }

                      else{
                        String[] array = fromInput.split(" ");
                        System.out.println("array[0]:" + array[0]);
                        System.out.println("array[1]:" + array[1]);
                        System.out.println("arry[2]:" + array[2]); 

                        String[] a = array[1].split("[?]");
                        System.out.println("a[0]:" + a[0]);
                        System.out.println("a[1]:" + a[1]);
                        //System.out.println("a[2]:" + a[2]);
                        
                        String[] b = a[1].split("[&]");
                        System.out.println("b[0]:" + b[0]);
                    
                        for(int i=0; i <b.length; i++){

                            if(b[i].contains("hostname")){
                                hostname = b[i].substring(b[i].indexOf("=") + 1);
                                System.out.println(hostname);
                            }

                            else if (b[i].contains("timeout")){
                                timeout = Integer.parseInt(b[i].substring(b[i].indexOf("=") + 1));
                                System.out.println(timeout);
                            }

                            else if(b[i].contains("limit")){
                                limit = Integer.parseInt(b[i].substring(b[i].indexOf("=") + 1));
                                System.out.println(limit);
                            }

                            else if(b[i].contains("shutdown")){
                                shutdown = Boolean.parseBoolean(b[i].substring(b[i].indexOf("=") + 1));
                                System.out.println(shutdown);
                            }

                            else if (b[i].contains("string")){
                                string = b[i].substring(b[i].indexOf("=") + 1);
                                System.out.println(string);
                            }
                            else if (b[i].contains("port")){
                                port = Integer.parseInt(b[i].substring(b[i].indexOf("=") +1));
                                System.out.println(port);
                            }
                        }
                     //System.out.println(string);
                        TCPClient client = new tcpclient.TCPClient(shutdown, limit, timeout);
                        byte[] result;
                        if(string == null){
                            result = client.askServer(hostname, port, new byte[0]);
                        }
                        else{
                            result = client.askServer(hostname, port, string.getBytes());
                        }
                        String hello = new String(result, StandardCharsets.UTF_8);
                        String ab = "HTTP/1.1 200 OK\r\n\r\n" + hello;
                        outputStream.write(ab.getBytes());
                        clientSocket.close();
                        
                      }
                }
            //}
            
        }
     
        catch (IOException e){
            System.out.println("catch Exception");
        }        
        // Your code here

    }
    
}
