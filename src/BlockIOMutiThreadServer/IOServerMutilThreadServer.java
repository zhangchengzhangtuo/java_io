package BlockIOMutiThreadServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2016/12/3.
 */
public class IOServerMutilThreadServer {

    public static void close(ServerSocket serverSocket){
        if(serverSocket!=null){
            try {
                serverSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String [] argv) throws Throwable{
        ServerSocket serverSocket=null;
        try{
            serverSocket=new ServerSocket();
            serverSocket.bind(new InetSocketAddress(20000));
        }catch (Exception e){
            e.printStackTrace();
        }


        while(true) {
            final Socket socket = serverSocket.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            InputStream inputStream = socket.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String msg = bufferedReader.readLine();
                            System.out.println("Received message " + msg);
//                        if(msg.equalsIgnoreCase("bye")){
//                            close(serverSocket);
//                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

    }
}
