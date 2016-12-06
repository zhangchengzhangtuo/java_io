package BlockIOThreadPool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/3.
 */
public class IOServerThreadPool {

    public static void main(String [] argv) throws Throwable{
        ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ServerSocket serverSocket =null;
        try {
            serverSocket=new ServerSocket();
            serverSocket.bind(new InetSocketAddress(21000));
        }catch (Exception e){

        }

        try{
            while(true){
                final Socket socket=serverSocket.accept();
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            try {
                                InputStream inputStream = socket.getInputStream();
                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                System.out.println("Received message " + bufferedReader.readLine());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }catch (Exception e){

        }
    }
}
