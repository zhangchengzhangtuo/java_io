package zerocopy;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

/**
 * Created by Administrator on 2016/12/2.
 */
public class NioClient {

    public static void main(String [] argv) throws Throwable{
        SocketChannel socketChannel=SocketChannel.open();
        InetSocketAddress address=new InetSocketAddress(18000);
        socketChannel.connect(address);

        String filePath="D:\\softwareInstall\\intellij\\workspace\\java_IO\\src\\zerocopy\\test.txt";
        RandomAccessFile file=new RandomAccessFile(filePath,"rw");
        FileChannel channel=file.getChannel();
        channel.transferTo(0,channel.size(),socketChannel);

        channel.close();
        file.close();
        socketChannel.close();
    }

}
