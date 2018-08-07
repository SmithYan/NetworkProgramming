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

		// �ڶ˿�8888��8889��������������ServerSocketChannel
		ServerSocketChannel ssChannel1 = ServerSocketChannel.open();
		ssChannel1.configureBlocking(false);
		ssChannel1.socket().bind(new InetSocketAddress(8888));

		ServerSocketChannel ssChannel2 = ServerSocketChannel.open();
		ssChannel2.configureBlocking(false);
		ssChannel2.socket().bind(new InetSocketAddress(8889));

		// ע������ServerSocketChannel
		ssChannel1.register(selector, SelectionKey.OP_ACCEPT);
		ssChannel2.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			selector.select();
			Iterator it = selector.selectedKeys().iterator();

			while (it.hasNext()) {
				SelectionKey selKey = (SelectionKey) it.next();
				// ɾ���õ���key����ʾɾ����Ӧ��channel
				it.remove();
				if (selKey.isAcceptable()) {
					// �õ�Key��Ӧ��ServerSocketChannel
					ServerSocketChannel ssChannel = (ServerSocketChannel) selKey.channel();
					// �õ��ͻ��������SocketChannel
					SocketChannel socketChannel = ssChannel.accept();

					if (socketChannel != null) {
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						buffer.put(new Date().toString().getBytes());
						// �ѵ�ǰʱ��д��
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
