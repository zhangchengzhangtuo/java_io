package ReactorThreadPool;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/3.
 */
public class NioServer {

    public static void main(String [] argv) throws Throwable{
        Selector selector= Selector.open();
        ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(23000));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            if(selector.selectNow()<0){
                continue;
            }
            Set<SelectionKey> keys=selector.selectedKeys();
            Iterator<SelectionKey> iterator=keys.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey=iterator.next();
                iterator.remove();
                if(selectionKey.isAcceptable()){
                    ServerSocketChannel acceptServerSocketChannel= (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel=acceptServerSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    SelectionKey readKey=socketChannel.register(selector,SelectionKey.OP_READ);
                    readKey.attach(new Processor());
                }else if(selectionKey.isReadable()){
                    Processor processor= (Processor) selectionKey.attachment();
                    processor.process(selectionKey);
                }
            }
        }
    }
}
