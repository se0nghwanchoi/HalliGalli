import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class Client extends JFrame {
	int cards;
	JLabel[] playerLabels = new JLabel[4];     //플레이어
	JButton[] playerButtons = new JButton[4];  //플레이어 버튼
	Image backgroundImage;
	
	 ImageIcon bellIcon = new ImageIcon("images/Bell.png");   //종 이미지
	 ImageIcon bellRIcon = new ImageIcon("images/BellR.png"); //종이 눌렸을때 이미지

	 JButton bellButton = new JButton(bellIcon); 
	 
	    // bellButton을 위한 ActionListener 구현
	 ActionListener bellActionListener = new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            bellButton.setIcon(bellRIcon); // 이미지 변경

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

         //플레이어 버튼 생성 
        for (int i = 0; i < 4; i++) {
            playerButtons[i] = new JButton("Card Open");
            playerButtons[i].setFont(new Font("Serif", Font.BOLD, 10)); 
            playerButtons[i].setSize(100, 25);
         
           
        }
        playerButtons[0].setLocation(340,280);
 		playerButtons[1].setLocation(690,280);
 		playerButtons[2].setLocation(340,580);
 		playerButtons[3].setLocation(690,580);
 		
 		for (int i = 0; i < 4; i++) {
 		add(playerButtons[i]);
 		}
 	
 		
 		//bell 버튼 설정
 		bellButton.setSize(bellIcon.getIconWidth(), bellIcon.getIconHeight());
        bellButton.setLocation(500, 250);
        bellButton.addActionListener(bellActionListener); // ActionListener 추가
        bellButton.setOpaque(false); // 배경색을 투명으로 설정
        bellButton.setContentAreaFilled(false); // 내용 영역의 채우기를 비활성화하여 투명하게 설정
        bellButton.setBorderPainted(false); // 테두리를 없애서 투명하게 설정
        add(bellButton);
    
        setSize(900, 700);
        setVisible(true);
        
	}
	
	
	
	public static void main(String[] args) {
		new Client();
		
		
	

		
	}
}
