package ReactorThreadPool;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/3.
 */
public class Processor {

    private static final ExecutorService executorService= Executors.newFixedThreadPool(5);

    public void process(final SelectionKey selectionKey){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int count = socketChannel.read(byteBuffer);
                    if(count<0){
                        socketChannel.close();
                        selectionKey.cancel();
                        return;
                    }else if(count==0){
                        return;
                    }
                    System.out.println(new String(byteBuffer.array()));
                    return;
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        });
    }
}
