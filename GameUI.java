import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

class mancalaClickablePit extends JPanel {
	int mancalaClickablePitNum;
	private ImageIcon pitImage;
	JButton pitButton;

	mancalaClickablePit(int pitNumber) {
		mancalaClickablePitNum = pitNumber;
		if(mancalaClickablePitNum == 1) {
			pitImage = new ImageIcon("bluePit.jpg");
		} else {
			pitImage = new ImageIcon("orangePit.jpg");
		}
		pitButton = new JButton(pitImage);
		
		//Make the background of the button invisible
		pitButton.setBorderPainted(false); 
		pitButton.setContentAreaFilled(false); 
		pitButton.setFocusPainted(false); 
		pitButton.setOpaque(false);
		
	}
	public JButton getButton() {
		return pitButton;
	}	
}

class mancalaCachePit extends JPanel {
	int mancalaCachePitNum;
	private ImageIcon pitImage;
	JButton pitButton;
	boolean ALMODE_IS_OFF;

	mancalaCachePit(int pitNumber) {
		mancalaCachePitNum = pitNumber;
		ALMODE_IS_OFF = true;
		if(mancalaCachePitNum == 1) {
			pitImage = new ImageIcon("largeBluePit.jpg");
		} else {
			pitImage = new ImageIcon("largeOrangePit.jpg");
		}
		pitButton = new JButton(pitImage);
		
		//Make the background of the button invisible
		pitButton.setBorderPainted(false); 
		pitButton.setContentAreaFilled(false); 
		pitButton.setFocusPainted(false); 
		pitButton.setOpaque(false);
		
	}
	public JButton getButton() {
		return pitButton;
	}	
	public void ALMODE(boolean status) { //True turns ALMODE on 
		ALMODE_IS_OFF = status;
	}
}

public class GameUI extends JPanel {
	private Game game; 
	private JFrame frame;	
    private JButton pit0, pit1, pit2, pit3, pit4, pit5, pit6, pit7, pit8, pit9, pit10, pit11, pit12, pit13;
	
    private ActionListener actions = new ActionListener()
    {
    	@Override
        public void actionPerformed(ActionEvent ae)
        {
            if (ae.getSource() == pit0)
            {
                System.out.println("SUCESS - 0");
            }
            else if (ae.getSource() == pit1)
            {
            	System.out.println("SUCESS - 1");
            }
            else if (ae.getSource() == pit2)
            {
            	System.out.println("SUCESS - 2");
            }
            else if (ae.getSource() == pit3)
            {
            	System.out.println("SUCESS - 3");
            }
            else if (ae.getSource() == pit4)
            {
            	System.out.println("SUCESS - 4");
            }
            else if (ae.getSource() == pit5)
            {
            	System.out.println("SUCESS - 5");
            }
            else if (ae.getSource() == pit6)
            {
            	System.out.println("SUCESS - 6");
            }
            else if (ae.getSource() == pit7)
            {
            	System.out.println("SUCESS - 7");
            }
            else if (ae.getSource() == pit8)
            {
            	System.out.println("SUCESS - 8");
            }
            else if (ae.getSource() == pit9)
            {
            	System.out.println("SUCESS - 9");
            }
            else if (ae.getSource() == pit10)
            {
            	System.out.println("SUCESS - 10");
            }
            else if (ae.getSource() == pit11)
            {
            	System.out.println("SUCESS - 11");
            }
            else if (ae.getSource() == pit12)
            {
            	System.out.println("SUCESS - 12");
            }
            else if (ae.getSource() == pit13)
            {
            	System.out.println("SUCESS - 13");
            }
        }
    }; 
	
	public void displayGUI(Game game) throws IOException {
		this.game = game;
		
		//Create Window
		JFrame window = new JFrame("Mancala");
		window.setSize(1266,687);
		window.setMaximumSize(new Dimension(1266,687));
		window.setMinimumSize(new Dimension(1200, 600));
		window.setResizable(true);
		
		//Set background
		JLabel background = new JLabel(new ImageIcon(ImageIO.read(new File("mancalaBoard.jpg"))));
		window.setContentPane(background);
		
		//Utilizing the GridBagLayout 
		window.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Global Constants used
		c.fill = GridBagConstraints.HORIZONTAL;
		
		/*Creating the Orange pits - The bits are put in backwards due to their orientation on the Game.java side
		*		12	11	10	9	8	7
		*13							6
		*		0	1	2	3	4	5*/
		c.gridy = 0;
		
		c.gridx = 1;
		mancalaClickablePit _pit12 = new mancalaClickablePit(2);
		pit12 = _pit12.getButton();
		window.add(_pit12.getButton(),c);
		_pit12.getButton().addActionListener(actions);
		
		c.gridx = 2;
		mancalaClickablePit _pit11 = new mancalaClickablePit(2);
		pit11 = _pit11.getButton();
		window.add(_pit11.getButton(),c);
		_pit11.getButton().addActionListener(actions);

		c.gridx = 3;
		mancalaClickablePit _pit10 = new mancalaClickablePit(2);
		pit10 = _pit10.getButton();
		window.add(_pit10.getButton(),c);
		_pit10.getButton().addActionListener(actions);

		c.gridx = 4;
		mancalaClickablePit _pit9 = new mancalaClickablePit(2);
		pit9 = _pit9.getButton();
		window.add(_pit9.getButton(),c);
		_pit9.getButton().addActionListener(actions);

		c.gridx = 5;
		mancalaClickablePit _pit8 = new mancalaClickablePit(2);
		pit8 = _pit8.getButton();
		window.add(_pit8.getButton(),c);
		_pit8.getButton().addActionListener(actions);

		c.gridx = 6;
		mancalaClickablePit _pit7 = new mancalaClickablePit(2);
		pit7 = _pit7.getButton();
		window.add(_pit7.getButton(),c);
		_pit7.getButton().addActionListener(actions);
		
		//Creating all the blue pits
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridy = 2;
		
		c.gridx = 1;
		mancalaClickablePit _pit0 = new mancalaClickablePit(1);
		pit0 = _pit0.getButton();
		window.add(_pit0.getButton(),c);
		_pit0.getButton().addActionListener(actions);
		
		c.gridx = 2;
		mancalaClickablePit _pit1 = new mancalaClickablePit(1);
		pit1 = _pit1.getButton();
		window.add(_pit1.getButton(),c);
		_pit1.getButton().addActionListener(actions);
		
		c.gridx = 3;
		mancalaClickablePit _pit2 = new mancalaClickablePit(1);
		pit2 = _pit2.getButton();
		window.add(_pit2.getButton(),c);
		_pit2.getButton().addActionListener(actions);
		
		c.gridx = 4;
		mancalaClickablePit _pit3 = new mancalaClickablePit(1);
		pit3 = _pit3.getButton();
		window.add(_pit3.getButton(),c);
		_pit3.getButton().addActionListener(actions);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		mancalaClickablePit _pit4 = new mancalaClickablePit(1);
		pit4 = _pit4.getButton();
		window.add(_pit4.getButton(),c);
		_pit4.getButton().addActionListener(actions);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		mancalaClickablePit _pit5 = new mancalaClickablePit(1);
		pit5 = _pit5.getButton();
		window.add(_pit5.getButton(),c);
		_pit5.getButton().addActionListener(actions);
		
		//Add in both user cache pits
		c.anchor = GridBagConstraints.CENTER; //Required reset due to PAGE_END
		c.gridy = 0;
		c.gridheight = 3;
		
		c.gridx = 7;
		mancalaCachePit _pit6 = new mancalaCachePit(1);
		pit6 = _pit6.getButton();
		window.add(_pit6.getButton(),c);
		_pit6.getButton().addActionListener(actions);

		c.gridx = 0;
		mancalaCachePit _pit13 = new mancalaCachePit(2);
		pit13 = _pit13.getButton();
		window.add(_pit13.getButton(),c);
		_pit13.getButton().addActionListener(actions);
		
		//Game Control Buttons
		c.insets = new Insets(100,0,0,0);
		c.gridheight = 1;	//Reset height to 1
		c.gridy = 1;
		
		c.gridx = 2;
		window.add(new JButton("New Game"), c);
		
		
		c.gridx = 3;
		window.add(new JButton("Button 2"), c);
		
		c.gridx = 4;
		window.add(new JButton("Button 3"), c);
		
		c.gridx = 5;
		window.add(new JButton("Button 4"), c);
		
		//window.
		
		//Exit options and visibility status
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}

