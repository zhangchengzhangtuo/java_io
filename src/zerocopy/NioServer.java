package zerocopy;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/2.
 */
public class NioServer{

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;


    public static void main(String [] argv) throws Throwable{
        NioServer nioServer=new NioServer();
        nioServer.initServer();
        nioServer.start();
    }


    public void initServer() throws Exception{
        selector=Selector.open();
        serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(18000));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start(){
        while(true){
            try{
                int selectNum=selector.select();
                if(selectNum>0){
                    Set<SelectionKey> keys=selector.selectedKeys();
                    Iterator<SelectionKey> iter=keys.iterator();
                    while(iter.hasNext()){
                        SelectionKey key=iter.next();
                        if(key.isAcceptable()){
                            doAcceptable(key);
                        }
                        if(key.isReadable()){
                            doReadMessage(key);
                        }
                        iter.remove();
                    }
                }
            }catch (Exception e){

            }
        }
    }

    public void doAcceptable(SelectionKey key) throws Exception{
        System.out.println("is Acceptable");
        ServerSocketChannel serverSocketChannel= (ServerSocketChannel) key.channel();
        SocketChannel socketChannel=serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
    }

    public void doReadMessage(SelectionKey key) throws Exception{
        System.out.println("is readable");
        SocketChannel socketChannel= (SocketChannel) key.channel();

        ByteBuffer byteBuffer= ByteBuffer.allocate(100);

        System.out.println("receive from client");
        int readNumber=socketChannel.read(byteBuffer);
        while(readNumber>0){
            byteBuffer.flip();
            byte [] tmp=new byte[byteBuffer.limit()];
            byteBuffer.get(tmp);
            System.out.println(new String(tmp,"utf-8"));
            byteBuffer.clear();
            readNumber=socketChannel.read(byteBuffer);
        }
        socketChannel.close();
    }

    public void stopServer(){
        try{
            if(selector!=null && selector.isOpen()){
                selector.close();
            }
            if(serverSocketChannel!=null&& serverSocketChannel.isOpen()){
                serverSocketChannel.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
