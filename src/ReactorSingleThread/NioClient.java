package ReactorSingleThread;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/3.
 */
public class NioClient {

    private SocketChannel socketChannel;

    private Selector selector;

    public static void main(String [] argv) throws Throwable{
        NioClient nioClient=new NioClient();
        try{
            nioClient.initClient();
            nioClient.start();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public void start() throws Throwable{
        while(true) {
            int select = selector.select();
            if (select > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    if(selectionKey.isConnectable()){
                        doConnect(selectionKey);
                    }
                    iterator.remove();
                }
            }
        }
    }

    public void doWrite(SelectionKey selectionKey) throws Throwable{
        System.out.println("now we write");
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            String msg = "Server,how are you?";
            ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes("UTF-8"));
            System.out.println(msg);
            socketChannel.write(byteBuffer);
//            socketChannel.register(selector, SelectionKey.OP_READ);
        }catch (Throwable e){
            if(socketChannel.isOpen()){
                socketChannel.close();
            }
            e.printStackTrace();
        }
    }

    public void doConnect(SelectionKey selectionKey) throws Throwable{
        System.out.println("we now connect");
        SocketChannel socketChannel= (SocketChannel) selectionKey.channel();
        while(!socketChannel.finishConnect()){

        }
        String msg = "Server,how are you?";
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes("UTF-8"));
        System.out.println(msg);
        socketChannel.write(byteBuffer);
    }

    public void initClient() throws Exception{
        InetSocketAddress address=new InetSocketAddress(22000);
        socketChannel=SocketChannel.open();

        selector=Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_CONNECT);

        socketChannel.connect(address);

    }

    public void stopServer(){
        try{
            if(selector!=null && selector.isOpen()){
                selector.close();
            }

            if(socketChannel!=null&&socketChannel.isOpen()){
                socketChannel.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
