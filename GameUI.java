import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
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
			pitImage = createNewPitImage("images/bluePit/bluePit4.jpg");
		} else {
			pitImage = createNewPitImage("images/orangePit/orangePit4.jpg");
		}
		pitButton = new JButton(pitImage);
		
		//Make the background of the button invisible
		pitButton.setBorderPainted(false); 
		pitButton.setContentAreaFilled(false); 
		pitButton.setFocusPainted(false); 
		pitButton.setOpaque(false);
		
	}
    ImageIcon createNewPitImage(String iconName) {
    	ImageIcon resizedImageIcon = new ImageIcon(iconName);
	    Image image = resizedImageIcon.getImage(); // transform it 
	    Image newimg = image.getScaledInstance(109, 134,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    resizedImageIcon = new ImageIcon(newimg);  // transform it back
	    return resizedImageIcon;
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
			pitImage = createNewCacheImage("images/blueCache/blueCache0.jpg");
		} else {
			pitImage = createNewCacheImage("images/orangeCache/orangeCache0.jpg");
		}
		pitButton = new JButton(pitImage);
		
		//Make the background of the button invisible
		pitButton.setBorderPainted(false); 
		pitButton.setContentAreaFilled(false); 
		pitButton.setFocusPainted(false); 
		pitButton.setOpaque(false);
		
	}
    ImageIcon createNewCacheImage(String iconName) {
    	ImageIcon resizedImageIcon = new ImageIcon(iconName);
	    Image image = resizedImageIcon.getImage(); // transform it 
	    Image newimg = image.getScaledInstance(109, 509,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    resizedImageIcon = new ImageIcon(newimg);  // transform it back
	    return resizedImageIcon;
    }
	public JButton getButton() {
		return pitButton;
	}	
	public void ALMODE(boolean status) { //True turns ALMODE on 
		ALMODE_IS_OFF = status;
	}
}

public class GameUI extends JPanel {
	private int numPits = -1;
	private int numSeeds = -1;
	private boolean isRandom = false;
	private Game game; 
	private JFrame frame;	
	private JFrame window;
	private JPanel welcome;
	private JButton random;
	private JButton[] buttons;
	private JButton[] pitOptions;
	private JButton[] seedOptions;
    private JButton pit0, pit1, pit2, pit3, pit4, pit5, pit6, pit7, pit8, pit9, pit10, pit11, pit12, pit13;
    private JButton begin;
    private JButton newGame;
    ImageIcon orangePit0, orangePit1, orangePit2, orangePit3, orangePit4, orangePit5, orangePit6, orangePit7,orangePit8, orangePit9;
    ImageIcon bluePit0, bluePit1, bluePit2, bluePit3, bluePit4, bluePit5, bluePit6, bluePit7, bluePit8, bluePit9;
    private int houseClicked;
    private boolean updateRequired = false;
    
    ImageIcon createNewPitImage(String iconName) {
    	ImageIcon resizedImageIcon = new ImageIcon(iconName);
	    Image image = resizedImageIcon.getImage(); // transform it 
	    Image newimg = image.getScaledInstance(109, 134,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    resizedImageIcon = new ImageIcon(newimg);  // transform it back
	    return resizedImageIcon;
    }
    ImageIcon createNewCacheImage(String iconName) {
    	ImageIcon resizedImageIcon = new ImageIcon(iconName);
	    Image image = resizedImageIcon.getImage(); // transform it 
	    Image newimg = image.getScaledInstance(109, 509,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    resizedImageIcon = new ImageIcon(newimg);  // transform it back
	    return resizedImageIcon;
    }
    ImageIcon createNewBackgroundImage(String iconName) {
    	ImageIcon resizedImageIcon = new ImageIcon(iconName);
	    Image image = resizedImageIcon.getImage(); // transform it 
	    Image newimg = image.getScaledInstance(1250, 650,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    resizedImageIcon = new ImageIcon(newimg);  // transform it back
	    return resizedImageIcon;
    }
    
	private ActionListener actions = new ActionListener()
    {
    	@Override
        public void actionPerformed(ActionEvent ae)
        {
 
            if (ae.getSource() == pit0)
            {
                updateRequired = true;
                houseClicked = 0;
            }
            else if (ae.getSource() == pit1)
            {
                updateRequired = true;
                houseClicked = 1;
            }
            else if (ae.getSource() == pit2)
            {
                updateRequired = true;
                houseClicked = 2;
            }
            else if (ae.getSource() == pit3)
            {
                updateRequired = true;
                houseClicked = 3;
            }
            else if (ae.getSource() == pit4)
            {
                updateRequired = true;
                houseClicked = 4;
            }
            else if (ae.getSource() == pit5)
            {
                updateRequired = true;
                houseClicked = 5;
            }
            else if (ae.getSource() == pit6)
            {
            }
            else if (ae.getSource() == pit7)
            {
                updateRequired = true;
                houseClicked = 7;
            }
            else if (ae.getSource() == pit8)
            {
                updateRequired = true;
                houseClicked = 8;
            }
            else if (ae.getSource() == pit9)
            {
                updateRequired = true;
                houseClicked = 9;
            }
            else if (ae.getSource() == pit10)
            {
                updateRequired = true;
                houseClicked = 10;
            }
            else if (ae.getSource() == pit11)
            {
                updateRequired = true;
                houseClicked = 11;
            }
            else if (ae.getSource() == pit12)
            {
                updateRequired = true;
                houseClicked = 12;
            }
            else if (ae.getSource() == pit13)
            {
            }
            else if (ae.getSource() == newGame) {
            	game.newGame();
            }
        }
}; 

private ActionListener choosePits = new ActionListener()
{
	@Override
    public void actionPerformed(ActionEvent ae) {
		
	if (ae.getSource() == pitOptions[0])
	{
		numPits = 4;
	}
	else if (ae.getSource() == pitOptions[1])
        {
            numPits = 5;
        }
        else if (ae.getSource() == pitOptions[2])
        {
        	numPits = 6;
        }
        else if (ae.getSource() == pitOptions[3])
        {
        	numPits = 7;
        }
        else if (ae.getSource() == pitOptions[4])
        {
        	numPits = 8;
        }
        else if (ae.getSource() == pitOptions[5]) 
        {
        	numPits = 9;
        }
		
		System.out.println("Number of pits: "+ numPits);
	}
	
};

private ActionListener chooseSeeds = new ActionListener()
{
	@Override
    public void actionPerformed(ActionEvent ae) {
		
	if (ae.getSource() == seedOptions[0])
	{
		numSeeds = 1;
	}
	else if (ae.getSource() == seedOptions[1])
        {
		numSeeds = 2;
	}
        else if (ae.getSource() == seedOptions[2])
        {
        	numSeeds = 3;
        }
        else if (ae.getSource() == seedOptions[3])
        {
        	numSeeds = 4;
        }
        else if (ae.getSource() == seedOptions[4])
        {
        	numSeeds = 5;
        }
        else if (ae.getSource() == seedOptions[5]) 
        {
        	numSeeds = 6;
        }
        else if (ae.getSource() == seedOptions[6])
        {
        	numSeeds = 7;
        }
        else if (ae.getSource() == seedOptions[7])
        {
        	numSeeds = 8;
        }
        else if (ae.getSource() == seedOptions[8])
        {
        	numSeeds = 9;
        }
        else if (ae.getSource() == seedOptions[9]) 
        {
        	numSeeds = 10;
        }
        else if (ae.getSource() == random)
        {
        	if (isRandom == true)
        	{
        		isRandom = false;
        	}
        	else 
        	{
        		isRandom = true;
        	}
        	System.out.println("Random!");
        }
        else if (ae.getSource() == begin)
        {
        	try {
    			displayGUI();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        	game.newGame(0, numPits, numSeeds);
        }
		System.out.println("Number of seeds: "+ numSeeds);
	}
	
};
	
	public void displayGUI(Game game) throws IOException {
		buttons = new JButton[14];
		
		//Set background
		JLabel background = new JLabel(createNewBackgroundImage("images/mancalaBoard.jpg"));
		window.setContentPane(background);
		
		//Utilizing the GridBagLayout 
		window.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Global Constants used
		c.fill = GridBagConstraints.HORIZONTAL;
		
		/*Creating the Orange pits - The bits are put in backwards due to their orientation on the Game.java side
		*	 12	11	10	9	8	7
		*13					       6
		*	 0	1	2	3	4	5*/
		c.gridy = 0;
		
		c.gridx = 1;
		mancalaClickablePit _pit12 = new mancalaClickablePit(2);
		pit12 = _pit12.getButton();
		buttons[12] = pit12;
		window.add(_pit12.getButton(),c);
		_pit12.getButton().addActionListener(actions);
		
		c.gridx = 2;
		mancalaClickablePit _pit11 = new mancalaClickablePit(2);
		pit11 = _pit11.getButton();
		buttons[11] = pit11;
		window.add(_pit11.getButton(),c);
		_pit11.getButton().addActionListener(actions);

		c.gridx = 3;
		mancalaClickablePit _pit10 = new mancalaClickablePit(2);
		pit10 = _pit10.getButton();
		buttons[10] = pit10;
		window.add(_pit10.getButton(),c);
		_pit10.getButton().addActionListener(actions);

		c.gridx = 4;
		mancalaClickablePit _pit9 = new mancalaClickablePit(2);
		pit9 = _pit9.getButton();
		buttons[9] = pit9;
		window.add(_pit9.getButton(),c);
		_pit9.getButton().addActionListener(actions);

		c.gridx = 5;
		mancalaClickablePit _pit8 = new mancalaClickablePit(2);
		pit8 = _pit8.getButton();
		buttons[8] = pit8;
		window.add(_pit8.getButton(),c);
		_pit8.getButton().addActionListener(actions);

		c.gridx = 6;
		mancalaClickablePit _pit7 = new mancalaClickablePit(2);
		pit7 = _pit7.getButton();
		buttons[7] = pit7;
		window.add(_pit7.getButton(),c);
		_pit7.getButton().addActionListener(actions);
		
		//Creating all the blue pits
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridy = 2;
		
		c.gridx = 1;
		mancalaClickablePit _pit0 = new mancalaClickablePit(1);
		pit0 = _pit0.getButton();
		buttons[0] = pit0;
		window.add(_pit0.getButton(),c);
		_pit0.getButton().addActionListener(actions);
		
		c.gridx = 2;
		mancalaClickablePit _pit1 = new mancalaClickablePit(1);
		pit1 = _pit1.getButton();
		buttons[1] = pit1;
		window.add(_pit1.getButton(),c);
		_pit1.getButton().addActionListener(actions);
		
		c.gridx = 3;
		mancalaClickablePit _pit2 = new mancalaClickablePit(1);
		pit2 = _pit2.getButton();
		buttons[2] = pit2;
		window.add(_pit2.getButton(),c);
		_pit2.getButton().addActionListener(actions);
		
		c.gridx = 4;
		mancalaClickablePit _pit3 = new mancalaClickablePit(1);
		pit3 = _pit3.getButton();
		buttons[3] = pit3;
		window.add(_pit3.getButton(),c);
		_pit3.getButton().addActionListener(actions);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		mancalaClickablePit _pit4 = new mancalaClickablePit(1);
		pit4 = _pit4.getButton();
		buttons[4] = pit4;
		window.add(_pit4.getButton(),c);
		_pit4.getButton().addActionListener(actions);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		mancalaClickablePit _pit5 = new mancalaClickablePit(1);
		pit5 = _pit5.getButton();
		buttons[5] = pit5;
		window.add(_pit5.getButton(),c);
		_pit5.getButton().addActionListener(actions);
		
		//Add in both user cache pits
		c.anchor = GridBagConstraints.CENTER; //Required reset due to PAGE_END
		c.gridy = 0;
		c.gridheight = 3;
		
		c.gridx = 7;
		mancalaCachePit _pit6 = new mancalaCachePit(1);
		pit6 = _pit6.getButton();
		buttons[6] = pit6;
		window.add(_pit6.getButton(),c);
		_pit6.getButton().addActionListener(actions);

		c.gridx = 0;
		mancalaCachePit _pit13 = new mancalaCachePit(2);
		pit13 = _pit13.getButton();
		buttons[13] = pit13;
		window.add(_pit13.getButton(),c);
		_pit13.getButton().addActionListener(actions);
		
		//Game Control Buttons
		c.insets = new Insets(100,0,0,0);
		c.gridheight = 1;	//Reset height to 1
		c.gridy = 1;
		
		c.gridx = 2;
		newGame = new JButton("New Game");
		window.add(newGame, c);
		newGame.addActionListener(actions);
		
		c.gridx = 3;
		window.add(new JButton("Button 2"), c);
		
		c.gridx = 4;
		window.add(new JButton("Button 3"), c);
		
		c.gridx = 5;
		window.add(new JButton("Button 4"), c);
		
		//Exit options and visibility status
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void displayWelcome(Game game) throws IOException {
		this.game = game;
		
		//Create Window
		window = new JFrame("Mancala");
		window.setSize(1266,687);
		window.setMaximumSize(new Dimension(1266,687));
		window.setMinimumSize(new Dimension(1200, 600));
		window.setResizable(true);
		
		welcome = new JPanel();
		welcome.setBackground(Color.ORANGE);
		
		JLabel welcomeLabel = new JLabel("Welcome to Kalah!");
		JLabel pitsLabel = new JLabel("Choose the number of pits");
		JLabel seedsLabel = new JLabel("Choose the number of seeds");
		JLabel randomLabel = new JLabel("Select if you would like the seeds to be randomized");
		
		welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 50));
		pitsLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		seedsLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		randomLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		
		pitOptions = new JButton[6];
		seedOptions = new JButton[10];
		random = new JButton("Randomize");
		begin = new JButton("Begin!");
		
		welcome.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridwidth = 10;
		c.fill = GridBagConstraints.CENTER;
		
		c.gridy = 0;
		welcome.add(welcomeLabel, c);
		
		c.gridy = 2;
		welcome.add(pitsLabel, c);
		
		c.gridy = 4;
		welcome.add(seedsLabel, c);
		
		c.gridy = 6;
		welcome.add(randomLabel, c);
		
		c.gridwidth = 1;
		c.gridy = 3;
		
		for (int i = 0; i < 6; i++) {
			c.gridx = i + 3;
			pitOptions[i] = new JButton(String.valueOf(i + 4));
			pitOptions[i].addActionListener(choosePits);
			welcome.add(pitOptions[i], c);
		}

		c.gridy = 5;
		
		for (int i = 0; i < 10; i++) {
			c.gridx = i + 1;
			seedOptions[i] = new JButton(String.valueOf(i + 1));
			seedOptions[i].addActionListener(chooseSeeds);
			welcome.add(seedOptions[i], c);
		}
		
		c.gridx = 5;
		c.gridy = 7;
		random.addActionListener(chooseSeeds);
		welcome.add(random, c);
		
		c.gridy = 9;
		begin.addActionListener(chooseSeeds);
		welcome.add(begin, c);
		
		window.add(welcome);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	int waitForClick() {
		int house;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(updateRequired == false) {
			house = -1;
		} else {
		updateRequired = false;
		house = houseClicked;
		System.out.println(house);
		}
		return house;
	}
	void updateBoard(int [] board) {
		//Update blue pits
		for(int i = 0; i < 6; ++i) {
			buttons[i].setIcon(createNewPitImage("images/bluePit/bluePit" + board[i] + ".jpg"));
		}
		
		//Update blue cache
		buttons[6].setIcon(createNewCacheImage("images/blueCache/blueCache" + board[6] + ".jpg"));
		
		//Update Orange Pits
		for(int i = 7; i < 13; ++i) {
			buttons[i].setIcon(createNewPitImage("images/orangePit/orangePit" + board[i] + ".jpg"));
		}
		
		//Update Orange Cache
		buttons[13].setIcon(createNewCacheImage("images/orangeCache/orangeCache" + board[13] + ".jpg"));
	}
}
