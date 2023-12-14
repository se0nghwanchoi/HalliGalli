import java.net.ServerSocket;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.net.Socket;

public class Server {

	static ArrayList<ServerThread> list = new ArrayList<>();

	static int clientCount = 0;

	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(9999);

		Socket s;

		while (true) {
			s = socket.accept();

			DataInputStream is = new DataInputStream(s.getInputStream());
			DataOutputStream os = new DataOutputStream(s.getOutputStream());

			ServerThread thread = new ServerThread(s, "client " + clientCount, is, os);
			list.add(thread);
			thread.start();
			clientCount++;

		}
	}
}

class ServerThread extends Thread {
	Scanner scn = new Scanner(System.in);
	private String name;
	final DataInputStream is;
	final DataOutputStream os;
	Socket s;
	boolean active;

	public ServerThread(Socket s, String name, DataInputStream is, DataOutputStream os) {
		this.is = is;
		this.os = os;
		this.name = name;
		this.s = s;
		this.active = true;
	}

	@Override
	public void run() {
        String message;
        try {
            while (true) {
                message = is.readUTF();
                if (message.equals("logout")) {
                    this.active = false;
                    break;
                }
                for (ServerThread t : Server.list) {
                    t.os.writeUTF(this.name + " : " + message);
                    t.os.flush();
                }
            }
        } catch (IOException e) {
            // 클라이언트와의 연결이 끊어졌을 때 예외 처리
            System.err.println("Client " + this.name + " disconnected.");
        } finally {
            // 클라이언트와의 연결이 끊어지면 해당 클라이언트를 리스트에서 제거
            Server.list.remove(this);
            try {
                this.is.close();
                this.os.close();
                this.s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}