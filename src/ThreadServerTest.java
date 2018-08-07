import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServerOneClient extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public ServerOneClient(Socket s) throws IOException {
		socket = s;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		start();
	}

	public void run() {
		try {
			while (true) {
				String str = in.readLine();
				if (str.equals("END"))
					break;
				System.out.println("Echoing:" + str);
				out.println(str);
			}
			System.out.println("closing...");
		} catch (IOException e) {
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}

public class ThreadServerTest {

	static final int PORT = 8888;

	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Server Start");
		try {
			while (true) {
				Socket socket = s.accept();
				try {
					new ServerOneClient(socket);
				} catch (IOException e) {
					socket.close();
				}
			}
		} finally {
			s.close();
		}
	}
}
