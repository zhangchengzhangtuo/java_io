package BlockIOSingleThreadServer;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2016/12/2.
 */
public class IOServerSingleThreadServer {

    private char [] recvBuf=new char[100];

    public static void main(String [] argv) throws Throwable{
        ServerSocket serverSocket=null;
        try{
            serverSocket=new ServerSocket();
            serverSocket.bind(new InetSocketAddress(19000));
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            Socket socket=serverSocket.accept();
            while(true){
                InputStream inputStream=socket.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                System.out.println("Received message "+ bufferedReader.readLine());
            }
        }catch (Exception e){
            try{
                serverSocket.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
    }
}
