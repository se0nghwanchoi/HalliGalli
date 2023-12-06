import java.net.ServerSocket;
import java.io.*;
import java.net.*;
import java.util.*;
import java.net.Socket;


public class Server { 
	try {
		ServerSocket listener = new ServerSocket(9999);
		Socket socket = listener.accept();
		System.out.println("서버에 연결되었습니다.");
	} catch (IOException e) {
		System.out.println(e.getMessage());
	}
}
