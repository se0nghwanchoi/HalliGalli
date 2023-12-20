import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JLabel;



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

class Card {
    private int fruit;
    private int number;
	JLabel[] cardLabels = new JLabel[4];      //카드

    public Card(int fruit, int number) {
        this.fruit = fruit;
        this.number = number;
    }

    public int getFruit() {
        return fruit;
    }

    public int getNumber() {
        return number;
    }
    @Override
    public String toString() {
        return "Fruit: " + getFruit() + ", Number: " + getNumber();
    }
}


class Table {
 	private List<Card> list = new LinkedList<>(); // 플레이어가 넘긴 카드 리스트
 	Card[] playerCard = new Card[4]; // 현재 보이는 카드


 	// 테이블에 카드 추가(플레이어가 TURN)
 	public void addTableCard(Card c, int playerId) {
 		list.add(0, c);
 		playerCard[playerId] = c;
 		
 	}
 	
 

// 종을 친 플레이어에게 테이블에 있는 카드들을 주고, 플레이어의 카드 장수를 갱신
    public void giveTableCardsToPlayer(int playerId, List<Card> playerCardList) {
        for (Card card : list) {
            playerCardList.add(card); // 종을 친 플레이어의 카드 리스트에 카드 추가
        }
        list.clear(); // 테이블의 카드 리스트 비우기

    }

 	// 테이블에 카드 제거(플레이어 BELL)
 	public Card removeTableCard() {
 		for (int i = 0; i < 4; i++) { // 보이는 카드 초기화
 			playerCard[i] = null;
 		}
 		return list.remove(0);
 	}

 	public void showTableList() {
 		System.out.println("TABLE : " + list);
 	}

 	public int size() {
 		return list.size();
 	}

 	public boolean sumFive(/* Manager mng */) { // 과일이 다섯개인지 확인해 주는 메소드
 		int[] sum = new int[4]; // 각 과일의 총 합

 		for (int i = 0; i < 4; i++)
 			if (playerCard[i] != null) // 뒤집은 카드만
 				sum[playerCard[i].getFruit()] += playerCard[i].getNumber();


 		for (int i = 0; i < 4; i++) {
 			if (sum[i] == 5) {
 				//System.out.println("동일 과일 5개");
 				return true;
 			}
 		}

 		
 		return false;
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
