package MutilReactor;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/5.
 */
public class Processor {

    private static final ExecutorService executorService=Executors.newFixedThreadPool(2*Runtime.getRuntime().availableProcessors());

    private Selector selector;

    public Processor() throws Exception{
        this.selector= SelectorProvider.provider().openSelector();
        start();
    }

    public void addChannel(SocketChannel socketChannel) throws Exception{
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

    public void start(){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                //epoll-bug,这个地方是NIO的一个bug，副作用就是while循环消耗CPU，在这个例子中，如果跑起来的话，CPU瞬间飙到100%
                while(true){
                    try {
                        if (selector.selectNow() <= 0) {
                            continue;
                        }
                        Set<SelectionKey> keys=selector.selectedKeys();
                        Iterator<SelectionKey> iterator=keys.iterator();
                        while(iterator.hasNext()){
                            SelectionKey key=iterator.next();
                            iterator.remove();
                            if(key.isReadable()){
                                ByteBuffer buffer=ByteBuffer.allocate(1024);
                                SocketChannel socketChannel= (SocketChannel) key.channel();
                                int count=socketChannel.read(buffer);
                                if(count<0){
                                    socketChannel.close();
                                    key.cancel();
                                    continue;
                                }else if(count==0){
                                    continue;
                                }else{
                                    System.out.println("recv msg:"+new String(buffer.array()));
                                }

                            }
                        }

                    }catch (Exception e){

                    }
                }
            }
        });
    }
}
