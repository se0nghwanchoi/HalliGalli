import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



public class Client extends JFrame /*implements Runnable, ActionListener*/ {
	int cards;
	JLabel[] playerLabels = new JLabel[4];     //플레이어
	//JButton[] playerButtons = new JButton[4];  //플레이어 버튼
	Image backgroundImage;
	JScrollPane 				scroll_Pane;
	JTextArea 					text_Area;
	JTextField 					text_Field; 
	
	ImageIcon bellIcon = new ImageIcon("images/Bell.png");   //종 이미지
	ImageIcon bellRIcon = new ImageIcon("images/BellR.png"); //종이 눌렸을때 이미지
	ImageIcon cardOpenIcon = new ImageIcon("images/card_open.png");    //카드 오픈 버튼 이미지
	ImageIcon cardOpenPIcon = new ImageIcon("images/card_open_p.png");  //카드 오픈 버튼이 눌렸을때
	
	File bgm = new File("sounds/bell.wav"); // 종 효과음
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    
    Clip clip;
	
	JButton bellButton = new JButton(bellIcon); 
	 
	    // bellButton을 위한 ActionListener 구현
	ActionListener bellActionListener = new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            bellButton.setIcon(bellRIcon); // 이미지 변경
	            
	            // 종 클릭했을 때 효과음 실행
	            try {
	            	stream = AudioSystem.getAudioInputStream(bgm);
	            	format = stream.getFormat();
	            	info = new DataLine.Info(Clip.class, format);
	            	clip = (Clip)AudioSystem.getLine(info);
	            	clip.open(stream);
	            	clip.start();
	            } catch (Exception ee) {
	            	System.out.println("music err");
	            }

	            // javax.swing.Timer 사용
	            javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    bellButton.setIcon(bellIcon); // 1초 후에 원래 이미지로 변경
	                }
	            });

	            timer.setRepeats(false); // 타이머를 한 번만 실행하도록 설정
	            timer.start(); // 타이머 시작
	        }
	    };
	    
		// 채팅 되는지 보려고 내가 임의로 추가했음!! 확인하면 주석은 지워도 됨 + 수정해도 됨
	    private void Chat() {
	        try {
	            Socket socket = new Socket("localhost", 9999); // 서버의 IP와 포트에 맞게 수정
	            DataInputStream is = new DataInputStream(socket.getInputStream());
	            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
	           
	            // 클라이언트에서 채팅 메시지를 서버로 전송하는 부분
	            text_Field.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    try {
	                        os.writeUTF(text_Field.getText());
	                        os.flush(); // 추가: 버퍼 비우기
	                        text_Field.setText("");
	                        //text_Field.setText("");
	                    } catch (IOException ex) {
	                        ex.printStackTrace();
	                    }
	                }
	            });

	            while (true) {
	                // 서버에서 받은 채팅 메시지를 JTextArea에 출력하는 부분
	                String message = is.readUTF();
	                text_Area.append(message + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	
	public Client() {
		setTitle("할리갈리");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); 

        
        class BackGround extends JPanel { //배경이미지 설정
        	ImageIcon icon = new ImageIcon("images/backG.jpg");
        	Image img = icon.getImage();
        	
        	public void paintComponent(Graphics g) {
        		super.paintComponent(g);
        		//이미지 크기에 맞춰 꽉차게 그리기
        		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        	}
        }
        
        BackGround backGround = new BackGround();
    	//backGround.setOpaque(false); //패널 불투명도 설정
    	setContentPane(backGround);

    	Container p = getContentPane();
    	p.setLayout(null);
    	
        // 플레이어 라벨 생성
        for (int i = 0; i < 4; i++) {
            playerLabels[i] = new JLabel("Player " + (i + 1));
            playerLabels[i].setFont(new Font("Serif", Font.BOLD, 18));
            playerLabels[i].setForeground(Color.BLACK);
            playerLabels[i].setSize(100, 50);

        }
    		playerLabels[0].setLocation(320,30);
    		playerLabels[1].setLocation(670,30);
    		playerLabels[2].setLocation(320,330);
    		playerLabels[3].setLocation(670,330);
    		
    	for (int i = 0; i < 4; i++) {
    		add(playerLabels[i]);
    	}

 	
 		
 		//bell 버튼 설정
 		bellButton.setSize(bellIcon.getIconWidth(), bellIcon.getIconHeight());
        bellButton.setLocation(480, 240);
        bellButton.addActionListener(bellActionListener); // ActionListener 추가
        bellButton.setOpaque(false); // 배경색을 투명으로 설정
        bellButton.setContentAreaFilled(false); // 내용 영역의 채우기를 비활성화하여 투명하게 설정
        bellButton.setBorderPainted(false); // 테두리를 없애서 투명하게 설정
        add(bellButton);
        
        
        //채팅창 설정        
        JPanel ChatPanel = new JPanel();
		ChatPanel.setLayout(new BorderLayout());
		ChatPanel.setLocation(30, 200);
		ChatPanel.setSize(229, 300);
	
		text_Area = new JTextArea("채팅창", 1, 1);
		scroll_Pane = new JScrollPane(text_Area);
		text_Area.setEditable(false);
		text_Field = new JTextField("");
		text_Field.setFont(new Font("Serif", Font.BOLD, 12));
		text_Field.setSize(229, 40);

		ChatPanel.add(scroll_Pane, BorderLayout.CENTER);
		ChatPanel.add(text_Field, BorderLayout.SOUTH);
		p.add(ChatPanel);
		
		 // 카드 뒤집기 버튼 추가
		JButton flipCardButton = new JButton(cardOpenIcon);
		flipCardButton.setSize(cardOpenIcon.getIconWidth(), cardOpenIcon.getIconHeight());
		flipCardButton.setLocation(30, 520);
		flipCardButton.setOpaque(false);
		flipCardButton.setContentAreaFilled(false);
		flipCardButton.setBorderPainted(false);
		flipCardButton.setBorder(null);
		flipCardButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        flipCardButton.setIcon(cardOpenPIcon); // 이미지 변경
		       
		        
		        // javax.swing.Timer 사용
		        javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		                flipCardButton.setIcon(cardOpenIcon); // 1초 후에 원래 이미지로 변경
		                
		            }
		        });

		        timer.setRepeats(false); // 타이머를 한 번만 실행하도록 설정
		        timer.start(); // 타이머 시작
		    }
		});
		p.add(flipCardButton);
		
		
		// 채팅창 상단에 게임 상황을 표시하는 패널 추가
		JPanel gameStatusPanel = new JPanel();
		//gameStatusPanel.setLayout(new BorderLayout());
		gameStatusPanel.setLocation(30, 110); // 상단에 배치할 예정이므로 적절한 위치 지정
		gameStatusPanel.setSize(229, 110); // 남은 공간만큼의 크기 설정
		// 예시로 JLabel을 사용하여 간단한 게임 상황 표시
		JLabel gameStatusLabel = new JLabel("다른 플레이어를 기다리는중..");
		gameStatusLabel.setFont(new Font("Serif", Font.BOLD, 16));
		gameStatusLabel.setForeground(Color.BLACK); 
		gameStatusPanel.add(gameStatusLabel);
		gameStatusPanel.setOpaque(false); // 배경을 투명하게 설정하여 채팅 창의 배경 이미지가 보이도록 함

		// 메인 패널에 게임 상황 패널 추가
		p.add(gameStatusPanel);
		
		

    
        setSize(900, 700);
        setVisible(true);
        
	}
	
	
	
	public static void main(String[] args) {
	    Client client = new Client();
        client.Chat();
		
	}
}
