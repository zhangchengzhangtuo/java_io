package BlockIOMutiThreadServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Administrator on 2016/12/2.
 */
public class IOClient {

    public static void main(String [] argv) throws Throwable{
        try{
            Socket socket=new Socket();
            socket.connect(new InetSocketAddress(20000));
//            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out=new PrintWriter(socket.getOutputStream());
            BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
            while(true){
                String msg=reader.readLine();
                out.println(msg);
                out.flush();
                if(msg.equals("bye")){
                    break;
                }
            }
            socket.close();
        }catch (Exception e){

        }
    }
}
