import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class ClientThread extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private static int COUNTER = 0;
	private int id = COUNTER++;
	private static int THREADCOUNT = 0;

	public static int threadCount() {
		return THREADCOUNT;
	}

	public ClientThread(InetAddress addr) {
		System.out.println("Making client" + id);
		THREADCOUNT++;
		try {
			socket = new Socket(addr, ThreadServerTest.PORT);
		} catch (IOException e) {
		}
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(
					new BufferedWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))), true);
			start();
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e2) {
			}
		}
	}

	public void run() {
		try {
			for (int i = 0; i < 25; i++) {
				out.println("Client " + id + ":" + i);
				String str = in.readLine();
				System.out.println(str);
			}
			out.println("END");
		} catch (Exception o) {

		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
			THREADCOUNT--;
		}
	}
}

public class MultiClientTest {
	static final int MAX_THREADS = 40;

	public static void main(String[] args) throws IOException, InterruptedException {
		InetAddress addr = InetAddress.getByName(null);
		while (true) {
			if (ClientThread.threadCount() < MAX_THREADS)
				new ClientThread(addr);
			Thread.sleep(100);
		}

	}

}
