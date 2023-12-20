import java.io.*;
import java.net.*;
import java.util.*;

// Vector 이용하여 각 클라이언트마다 저장할 수 있도록 지정
public class Server {
    static Vector<ClientInfo> clients = new Vector<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);

        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());

            ClientInfo clientInfo = new ClientInfo("플레이어 " + (clients.size() + 1), is, os, socket);
            clients.add(clientInfo);

            ServerThread thread = new ServerThread(clientInfo);
            thread.start();
        }
    }
}

//각 클라이언트 정보
class ClientInfo {
    String name;
    DataInputStream is;
    DataOutputStream os;
    Socket socket;

    public ClientInfo(String name, DataInputStream is, DataOutputStream os, Socket socket) {
        this.name = name;
        this.is = is;
        this.os = os;
        this.socket = socket;
    }
}

class ServerThread extends Thread {
    private ClientInfo clientInfo;

    public ServerThread(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public void run() {
        try {
            broadcast(clientInfo.name + " 님이 입장하셨습니다.");

            String message;
            while (true) {
                message = clientInfo.is.readUTF();

                if (message.equals("로그아웃")) {
                    broadcast(clientInfo.name + " 님이 방을 나가셨습니다.");
                    break;
                } else if (message.startsWith("JLabelChange:")) {
                	// "JLabelChange:"로 시작하는 메시지를 처리하고 JLabel 값을 변경하도록 함
                	String newLabel = message.substring("JLabelChange:".length());
                    //labelChange(newLabel);
                    broadcast("JLabel 값이 변경되었습니다: " + newLabel);
                } else {
                    broadcast(clientInfo.name + " : " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            synchronized (Server.clients) {
                Server.clients.remove(clientInfo);
            }
            try {
                clientInfo.is.close();
                clientInfo.os.close();
                clientInfo.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Server.clients.isEmpty()) {
                System.out.println("모든 플레이어가 퇴장하였습니다.");
                System.exit(0);
            }
        }
    }

    //모든 클라이언트에 변경값 알림
    public void broadcast(String message) {
        synchronized (Server.clients) {
            for (ClientInfo client : Server.clients) {
                try {
                	if(message.startsWith("JLabel 값이 변경되었습니다: ")) {
                    	//String newLabel = message.substring("JLabel 값이 변경되었습니다: ".length());
                        client.os.writeUTF(message);
                        client.os.flush();
                	}
                	else {
                        client.os.writeUTF(message);
                        client.os.flush();
                	}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void labelChange(String msg) {
        try {
            clientInfo.os.writeUTF(msg);
            clientInfo.os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
