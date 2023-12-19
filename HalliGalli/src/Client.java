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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	//int cards;
	JLabel[] playerLabels = new JLabel[4];     //플레이어
	//JButton[] playerButtons = new JButton[4];  //플레이어 버튼
	JLabel[] cardLabels = new JLabel[4];      //카드
	Image backgroundImage;
	JScrollPane 				scroll_Pane;
	JTextArea 					text_Area;
	JTextField 					text_Field; 
	
	Table table = new Table(); // Table 객체 생성
	Player[] players = new Player[4]; // 플레이어 배열 생성
	JLabel[] cardcountLabels = new JLabel[4];
	
	
	
	 ArrayList<Card> deck; // 카드 덱을 저장할 리스트
	 int currentPlayer = 0; // 플레이어 순서를 저장하는 변수
	// int[] playerCardCount = {14, 14, 14, 14}; // 4명의 플레이어가 각각 14장의 카드를 가짐
	 ArrayList<Card> tableCards = new ArrayList<>(); // 테이블 카드를 저장할 리스트
	
	ImageIcon cardbackIcon = new ImageIcon("images/back.png"); //카드 뒷면 이미지
	int bwidth = cardbackIcon.getIconWidth(); // 이미지 폭
	int bheight = cardbackIcon.getIconHeight(); // 이미지 높이
	ImageIcon bellIcon = new ImageIcon("images/Bell.png");   //종 이미지
	ImageIcon bellRIcon = new ImageIcon("images/BellR.png"); //종이 눌렸을때 이미지
	ImageIcon cardOpenIcon = new ImageIcon("images/card_open.png");    //카드 오픈 버튼 이미지
	ImageIcon cardOpenPIcon = new ImageIcon("images/card_open_p.png");  //카드 오픈 버튼이 눌렸을때
	
	File bgm = new File("sounds/bell.wav"); // 종 효과음
    AudioInputStream str;
    DataLine.Info info;
    
    Clip clip;
	
	JButton bellButton = new JButton(bellIcon); 
	 

	    
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
		 // 카드 덱 초기화
        initializeDeck();	
        
		setTitle("할리갈리");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); 
        
        // Player 객체 생성 후 players 배열에 추가
        for (int i = 0; i < 4; i++) {
            players[i] = new Player();
        }
       
       
        
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
    	
    	//카드장수
    	 for (int i = 0; i < 4; i++) {
             cardcountLabels[i] = new JLabel((players[i].getHand().size()) + "장 ");
             cardcountLabels[i].setFont(new Font("Serif", Font.BOLD, 18));
             cardcountLabels[i].setForeground(Color.BLACK);
             cardcountLabels[i].setSize(100, 50);

         }
    	 cardcountLabels[0].setLocation(400,30);
    	 cardcountLabels[1].setLocation(750,30);
    	 cardcountLabels[2].setLocation(400,330);
    	 cardcountLabels[3].setLocation(750,330);
     		
     	for (int i = 0; i < 4; i++) {
     		add(cardcountLabels[i]);
     	}
    	
    	//카드 뒷면 배치
    	 for (int i = 0; i < 4; i++) {
             cardLabels[i] = new JLabel(cardbackIcon);
             cardLabels[i].setSize(bwidth, bheight); 
             

         }
     		cardLabels[0].setLocation(320,80);
     		cardLabels[1].setLocation(670,80);
     		cardLabels[2].setLocation(320,380);
     		cardLabels[3].setLocation(670,380);
     		
     	for (int i = 0; i < 4; i++) {
     		add(cardLabels[i]);
     	}
    	
     	
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

 	
 		
 		//bell 버튼 설정
 		bellButton.setSize(bellIcon.getIconWidth(), bellIcon.getIconHeight());
        bellButton.setLocation(525, 250);
        bellButton.setOpaque(false); // 배경색을 투명으로 설정
        bellButton.setContentAreaFilled(false); // 내용 영역의 채우기를 비활성화하여 투명하게 설정
        bellButton.setBorderPainted(false); // 테두리를 없애서 투명하게 설정
	
        bellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bellButton.setIcon(bellRIcon); // 이미지 변경
                // 종 클릭했을 때 효과음 실행하도록 설정
                
               if(!table.sumFive()) {      //종치기 실패     	   
            	   // 다른 플레이어들에게 카드를 한 장씩 주는 로직 추가
            	   switch (currentPlayer) {
                   case 0:
                       //System.out.println("플레이어 1 종치기 실패");
                	   gameStatusLabel.setText("Player 1 종치기 실패");
                       break;
                   case 1:
                       //System.out.println("플레이어 2가 종치기를 실패했습니다.");
                	   gameStatusLabel.setText("Player 2 종치기 실패");
                       break;
                   case 2:
                       //System.out.println("플레이어 3가 종치기를 실패했습니다.");
                	   gameStatusLabel.setText("Player 3 종치기 실패");
                       break;
                   case 3:
                       //System.out.println("플레이어 4가 종치기를 실패했습니다.");
                	   gameStatusLabel.setText("Player 4 종치기 실패");
                       break;
                   default:
                       break;
               }
            	   for (int i = 0; i < 4; i++) {
            		    // 현재 플레이어에게 카드를 주지 않도록 함 (현재 플레이어의 인덱스는 currentPlayer에 저장되어 있음)
            		    if (i != currentPlayer) {
            		        if (!players[i].getHand().isEmpty()) {
            		            
            		            Card drawnCard = players[currentPlayer].getHand().remove(players[currentPlayer].getHand().size() - 1);            		            
            		            players[i].addToHand(drawnCard);
            		            // 플레이어의 카드 장수 갱신
            		            cardcountLabels[i].setText((players[i].getHand().size()) + "장 ");
            		            cardcountLabels[currentPlayer].setText((players[currentPlayer].getHand().size()) + "장 ");
            		            
            		            
            		        } else {
            		            System.out.println("플레이어 " + (i + 1) + "의 핸드에 카드가 없습니다.");
            		            // 해당 플레이어의 핸드에 카드가 없는 경우, 이에 대한 추가 처리를 진행할 수 있습니다.
            		        }
            		    }
            		}
               }
               else {			//종치기 성공
            	   switch (currentPlayer) {
                   case 0:
                       //System.out.println("플레이어 1가 종을 성공적으로 쳤습니다.");
                	   gameStatusLabel.setText("Player 1 종치기 성공");
                       break;
                   case 1:
                       //System.out.println("플레이어 2가 종을 성공적으로 쳤습니다.");
                	   gameStatusLabel.setText("Player 2 종치기 성공");
                       break;
                   case 2:
                       //System.out.println("플레이어 3가 종을 성공적으로 쳤습니다.");
                	   gameStatusLabel.setText("Player 3 종치기 성공");
                       break;
                   case 3:
                       //System.out.println("플레이어 4가 종을 성공적으로 쳤습니다.");
                	   gameStatusLabel.setText("Player 4 종치기 성공");
                       break;
                   default:
                       break;
               }
            	   table.removeTableCard();
            	   // 테이블에 있는 카드를 해당 플레이어에게 주고 카드 장수 갱신
            	    table.giveTableCardsToPlayer(currentPlayer, players[currentPlayer].getHand());
            	    

            	 // 종을 친 플레이어의 카드 장수를 갱신하여 UI에 반영
            	    int newCardsCount = table.size() + 1; // 새로 얻은 카드의 수를 가져옴
            	    int currentPlayerCardCount = players[currentPlayer].getHand().size() + newCardsCount; // 현재 총 카드 수

            	    // 플레이어의 손패에 새로운 카드를 추가하여 카드 장수 갱신
            	   

            	    // 종을 친 플레이어의 카드 장수만 갱신하여 UI에 반영
            	    cardcountLabels[currentPlayer].setText((players[currentPlayer].getHand().size()) + "장 ");
            	// 전체 플레이어의 cardLabels에 back 이미지를 설정
            	    for (int i = 0; i < 4; i++) {
            	        cardLabels[i].setIcon(cardbackIcon);
            	        if (players[i].getHand().isEmpty()) {
            	            // 플레이어의 핸드가 비어있으면 죽음 처리 혹은 다음 게임에 관여하지 않도록 설정할 수 있음
            	        	System.out.println("Player" + (i+1) + "이 죽음");
            	        }
            	    }
            	   
               }
                try {
                    str = AudioSystem.getAudioInputStream(bgm);
                    info = new DataLine.Info(Clip.class, str.getFormat());
                    clip = (Clip) AudioSystem.getLine(info);
                    clip.open(str);
                    clip.start();
                } catch (Exception ee) {
                    System.out.println("music error");
                }

             
             

                // javax.swing.Timer 사용하여 이미지 변경
                javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        bellButton.setIcon(bellIcon); // 1초 후에 원래 이미지로 변경
                    }
                });

                timer.setRepeats(false); // 타이머를 한 번만 실행하도록 설정
                timer.start(); // 타이머 시작
            }
        });

        
        p.add(bellButton);

        
        //채팅창 설정        
        JPanel ChatPanel = new JPanel();
		ChatPanel.setLayout(new BorderLayout());
		ChatPanel.setLocation(30, 200);
		ChatPanel.setSize(229, 300);
	
		text_Area = new JTextArea("", 1, 1);
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
		
		
		// flipCardButton의 ActionListener 안에 있는 actionPerformed 메서드 내용
		// flipCardButton의 ActionListener 안에 있는 actionPerformed 메서드
		flipCardButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // 현재 플레이어가 턴을 진행하는지 확인 (isPlayerTurn 메서드 사용)
		      
		            flipCardButton.setIcon(cardOpenPIcon);

		            
		            // 현재 플레이어의 핸드에서 카드 뒤집기
		            if (!players[currentPlayer].getHand().isEmpty()) {
		            Card flippedCard = players[currentPlayer].getHand().remove(players[currentPlayer].getHand().size() - 1);
		            System.out.println("뒤집힌 카드: " + flippedCard.getFruit() + " " + flippedCard.getNumber());

		            // 뒤집은 카드의 이미지 설정
		            ImageIcon flippedCardIcon = getCardImageIcon(flippedCard.getFruit(), flippedCard.getNumber());

		            // 각 플레이어 라벨에 카드 이미지 설정 (현재 플레이어만 설정)
		            cardLabels[currentPlayer].setIcon(flippedCardIcon);

		            // 테이블에 카드 추가
		            table.addTableCard(flippedCard, currentPlayer);
		            
		            table.showTableList(); //체크

		        
		            cardcountLabels[currentPlayer].setText((players[currentPlayer].getHand().size()) + "장 ");
		            } else {
		                System.out.println("핸드에 카드가 없습니다.");
		            }

		            currentPlayer = (currentPlayer + 1) % 4; // 다음 플레이어로 넘어감
		         // 현재 플레이어의 핸드가 비어있거나 다음 플레이어로 넘어가는 조건 확인
		            if (players[currentPlayer].getHand().isEmpty()) {
		                System.out.println("플레이어 " + (currentPlayer + 1) + "의 핸드에 카드가 없습니다.");
		                
		            }

			        javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
			            @Override
			            public void actionPerformed(ActionEvent e) {
			                flipCardButton.setIcon(cardOpenIcon);
			            }
			            
			        });

			        timer.setRepeats(false); // 타이머를 한 번만 실행하도록 설정
			        timer.start(); // 타이머 시작
			    }
			});


		p.add(flipCardButton);
		
		
		
    
        setSize(900, 700);
        setVisible(true);
        
	}
	
	   ImageIcon getCardImageIcon(int i, int number) {
	        String imagePath = "images/" + i + number + ".png"; // 이미지 경로 설정
	        return new ImageIcon(imagePath); // 해당 이미지 아이콘 반환
	    }
	  	  
	  void initializeDeck() {
		    deck = new ArrayList<>();
		    int[] fruits = {0, 1, 2, 3}; // 0: banana, 1: lime, 2: plum, 3: strawberry

		   
		    for (int fruit : fruits) {
		        // 1부터 4까지의 숫자는 각각 3장씩 추가
		        for (int i = 1; i <= 4; i++) {
		            for (int j = 0; j < 3; j++) {
		            	
		                deck.add(new Card(fruit, i));
		            }
		        }
		        // 숫자 5는 2장씩 추가
		        for (int j = 0; j < 2; j++) {
		            deck.add(new Card(fruit, 5));
		        }
		    }

		    Collections.shuffle(deck);
		    
		}


	     class Card {
	        private int fruit;
	        private int number;

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
	    
	   //테이블의 정보를 담은 클래스
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

	            // 모든 플레이어의 카드 장수 출력
	            for (int i = 0; i < 4; i++) {
	                int cardCount = players[i].getHand().size();
	                System.out.println("플레이어 " + (i + 1) + "의 장수: " + cardCount);
	            }
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
	     				System.out.println("동일 과일 5개");
	     				return true;
	     			}
	     		}

	     		
	     		return false;
	     	}
	     }


	    
	     
	     public class Player {
	          List<Card> hand; // 플레이어가 가진 카드 리스트
	         
	         public Player() {
	        	 
	             hand = new ArrayList<>();
	             initializeHandWithCards();
	          
	         }

	         public List<Card> getHand() {
	             return hand;
	             
	         }
	         private void initializeHandWithCards() {
	             for (int i = 0; i < 14; i++) {
	                 Card card = deck.remove(0); // 덱에서 카드 하나를 뽑아옴
	                 hand.add(card);
	             }
	         }
	         	      
	         public void addToHand(Card card) {
	             hand.add(card);
	         }

	         // 기타 필요한 메서드들을 추가로 구현할 수 있습니다.
	     }


	    boolean isPlayerTurn(int playerNumber) {
	        // 특정 플레이어의 차례인지 확인하는 메서드 (여러 조건을 고려하여 구현)
	        // 예를 들어, 해당 플레이어가 차례인지 여부를 확인하여 참/거짓 반환
	        // 여기에 특정 조건을 추가하여 해당 플레이어의 차례 여부를 반환합니다.
	        return true; // 또는 false로 변경하여 해당 플레이어의 차례 여부를 반환
	    }


	    public static void main(String[] args) {
	        Client client = new Client();
	        client.Chat();
	    }
}