import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

public class NBTimeServer {

	public NBTimeServer() throws Exception {
		acceptConnections();
	}

	private static void acceptConnections() throws Exception {
		Selector selector = Selector.open();

		// 在端口8888和8889绑定两个非阻塞的ServerSocketChannel
		ServerSocketChannel ssChannel1 = ServerSocketChannel.open();
		ssChannel1.configureBlocking(false);
		ssChannel1.socket().bind(new InetSocketAddress(8888));

		ServerSocketChannel ssChannel2 = ServerSocketChannel.open();
		ssChannel2.configureBlocking(false);
		ssChannel2.socket().bind(new InetSocketAddress(8889));

		// 注册两个ServerSocketChannel
		ssChannel1.register(selector, SelectionKey.OP_ACCEPT);
		ssChannel2.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			selector.select();
			Iterator it = selector.selectedKeys().iterator();

			while (it.hasNext()) {
				SelectionKey selKey = (SelectionKey) it.next();
				// 删除得到的key，表示删除对应的channel
				it.remove();
				if (selKey.isAcceptable()) {
					// 得到Key对应的ServerSocketChannel
					ServerSocketChannel ssChannel = (ServerSocketChannel) selKey.channel();
					// 得到客户端请求的SocketChannel
					SocketChannel socketChannel = ssChannel.accept();

					if (socketChannel != null) {
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						buffer.put(new Date().toString().getBytes());
						// 把当前时间写入
						socketChannel.write(buffer);
						socketChannel.close();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			NBTimeServer nbt = new NBTimeServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
