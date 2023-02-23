package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    private boolean shutdown;
    private Integer timeout;
    private Integer limit;
    
    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
      this.shutdown = shutdown;
      this.timeout = timeout;
      this.limit = limit;
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        ByteArrayOutputStream fromServer = new ByteArrayOutputStream();
        Socket clientSocket = new Socket (hostname, port); //här skapar vi en socket connection
        try {


            // här connectar vi vår timeout med socket
            if (this.timeout != null) {
                // TODO timeout 
                clientSocket.setSoTimeout(this.timeout); // Här connectar vi vår timeout till socket
            }



           // här implementerar jag shutdown metoden
            clientSocket.getOutputStream().write(toServerBytes, 0, toServerBytes.length);

            if (shutdown){
                 clientSocket.shutdownOutput();
            }

            byte[] hello = new byte[10];
            int check = 0;
            while ((check = clientSocket.getInputStream().read(hello)) != -1)
            {
                //här kollar jag limiten är null
                if(limit == null){
                    fromServer.write(hello, 0, check);
                }
                // här kollar jag om limit är inte null men är mindre eller lika med check
               else if((limit !=null) && ((limit - fromServer.size()) >= check)){
                    fromServer.write(hello, 0, check);
                    //clientSocket.close();
                }
                else
                {
                    fromServer.write(hello, 0, (limit - fromServer.size()));
                    break;

                 }
                    
            }
            
            //clientSocket.close(); // TODO shutdown server

        }
        catch (IOException e) 
        {
            System.out.println("everything is perfect");
            clientSocket.shutdownInput();
        }
        return fromServer.toByteArray();
    }
    
}
