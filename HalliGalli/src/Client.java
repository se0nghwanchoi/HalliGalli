import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class SetBack extends JPanel {
	ImageIcon icon = new ImageIcon("images/backG.jpg");
	Image img = icon.getImage();

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, this);
	}
}

public class Client extends JFrame {
	int cards;
	JLabel[] playerLabels = new JLabel[4];
	JButton[] playerButtons = new JButton[4];
	Image backgroundImage;
	
	public Client() {
		setTitle("할리갈리");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // 절대 위치 배치를 위해 null 레이아웃 사용
        
        // 플레이어 라벨 생성
        for (int i = 0; i < 4; i++) {
            playerLabels[i] = new JLabel("Player " + (i + 1));
            playerLabels[i].setFont(new Font("Serif", Font.BOLD, 18));
            playerLabels[i].setForeground(Color.BLACK);
            playerLabels[i].setSize(100, 50);

        }
    		playerLabels[0].setLocation(50,30);
    		playerLabels[1].setLocation(450,30);
    		playerLabels[2].setLocation(50,330);
    		playerLabels[3].setLocation(450,330);
    		
    	for (int i = 0; i < 4; i++) {
    		add(playerLabels[i]);
    	}

         //플레이어 버튼 생성 
        for (int i = 0; i < 4; i++) {
            playerButtons[i] = new JButton("Card Open");
            playerButtons[i].setFont(new Font("Serif", Font.BOLD, 10)); 
            playerButtons[i].setSize(100, 25);
         
           
        }
         playerButtons[0].setLocation(70,280);
 		playerButtons[1].setLocation(470,280);
 		playerButtons[2].setLocation(70,580);
 		playerButtons[3].setLocation(470,580);
 		
 	for (int i = 0; i < 4; i++) {
 		add(playerButtons[i]);
 	}

        setSize(900, 700);
        setVisible(true);
        
	}
	
	
	
	public static void main(String[] args) {
		new Client();
		//System.out.println("??");
		
	}

}
