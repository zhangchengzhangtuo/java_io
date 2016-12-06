package MutilReactor;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/12/5.
 */
public class NioServer {

    public static void main(String [] argv) throws Throwable{
        Selector selector=Selector.open();
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(24000));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        int coreNumber=Runtime.getRuntime().availableProcessors();
        Processor [] processors=new Processor[coreNumber];
        for(int i=0;i<processors.length;i++){
            processors[i]=new Processor();
        }
        int index=0;
        while(selector.select()>0){
            Set<SelectionKey> keys=selector.selectedKeys();
            for(SelectionKey key:keys){
                keys.remove(key);
                if(key.isAcceptable()){
                    ServerSocketChannel acceptServerSocketChannel= (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel=acceptServerSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    Processor processor=processors[(int)((index++)/coreNumber)];
                    processor.addChannel(socketChannel);
                }
            }
        }


    }
}
