package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    
    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        ByteArrayOutputStream fromServer = new ByteArrayOutputStream();
        try {
            Socket clientSocket = new Socket (hostname, port);
            clientSocket.getOutputStream().write(toServerBytes, 0, toServerBytes.length);
            
            byte[] hello = new byte[10];
            int check = 0;
            
            while ((check = clientSocket.getInputStream().read(hello)) != -1)
            {
                fromServer.write(hello, 0, check);
            }
            clientSocket.close();

        }
        catch (IOException e) 
        {
            System.out.println("everything is perfect");
        }
        return fromServer.toByteArray();
    }
}
