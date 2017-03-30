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

/*****Beginning of Helper Classes*****/
class ImagePanel extends JPanel {
	/**Used for creating background panels
	 *NOTE: The serialVersionUID is used for version identification
	 *(Make sure that the GUI and Game are on the 'same page')
	 */
	private static final long serialVersionUID = 1; //Used for version identification (Make sure that the GUI and Game are on the 'same page')
	private Image img;

	  public ImagePanel(String img) {
	    this(new ImageIcon(img).getImage());
	  }

	  public ImagePanel(Image img) {
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	  }

	  public void paintComponent(Graphics g) {
	    g.drawImage(img, 0, 0, null);
	  }

	}

class mancalaClickableHouse extends JPanel {
	/**Creates the Houses which users/AI interact with
	 *NOTE: The serialVersionUID is used for version identification
	 *(Make sure that the GUI and Game are on the 'same page')
	 */
	private static final long serialVersionUID = 1;
	int mancalaClickableHouseNum;
	private ImageIcon houseImage;
	JButton houseButton;

	mancalaClickableHouse(int houseNumber, int initialValue) {
		mancalaClickableHouseNum = houseNumber;
		if(mancalaClickableHouseNum == 1) {
			houseImage = createNewHouseImage("images/blueHouse.jpg");
		} else {
			houseImage = createNewHouseImage("images/orangeHouse.jpg");
		}
		houseButton = new JButton(houseImage);
		houseButton.setHorizontalTextPosition(JButton.CENTER);
		houseButton.setVerticalTextPosition(JButton.CENTER);
		houseButton.setText(Integer.toString(initialValue));
		
		//Make the background of the button invisible
		houseButton.setBorderPainted(false); 
		houseButton.setContentAreaFilled(false); 
		houseButton.setFocusPainted(false); 
		houseButton.setOpaque(false);
		
	}
    ImageIcon createNewHouseImage(String iconName) {
    	ImageIcon resizedImageIcon = new ImageIcon(iconName);
	    Image image = resizedImageIcon.getImage(); // transform it 
	    Image newimg = image.getScaledInstance(109, 134,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    resizedImageIcon = new ImageIcon(newimg);  // transform it back
	    return resizedImageIcon;
    }
	public JButton getButton() {
		return houseButton;
	}	
}

class mancalaCacheHouse extends JPanel {
	/**Creates the user point houses. IE the end left and right houses
	 *NOTE: The serialVersionUID is used for version identification
	 *(Make sure that the GUI and Game are on the 'same page')
	 */
	private static final long serialVersionUID = 1; //Used for version identification (Make sure that the GUI and Game are on the 'same page')
	int mancalaCacheHouseNum;
	private ImageIcon houseImage;
	JButton houseButton;
	boolean ALMODE_IS_OFF;

	mancalaCacheHouse(int houseNumber) {
		mancalaCacheHouseNum = houseNumber;
		ALMODE_IS_OFF = true;
		if(mancalaCacheHouseNum == 1) {
			houseImage = createNewCacheImage("images/blueCache.jpg");
		} else {
			houseImage = createNewCacheImage("images/orangeCache.jpg");
		}
		houseButton = new JButton(houseImage);
		houseButton.setHorizontalTextPosition(JButton.CENTER);
		houseButton.setVerticalTextPosition(JButton.CENTER);
		houseButton.setText(Integer.toString(0));
		
		//Make the background of the button invisible
		houseButton.setBorderPainted(false); 
		houseButton.setContentAreaFilled(false); 
		houseButton.setFocusPainted(false); 
		houseButton.setOpaque(false);
		
	}
    ImageIcon createNewCacheImage(String iconName) {
    	ImageIcon resizedImageIcon = new ImageIcon(iconName);
	    Image image = resizedImageIcon.getImage(); // transform it 
	    Image newimg = image.getScaledInstance(109, 509,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    resizedImageIcon = new ImageIcon(newimg);  // transform it back
	    return resizedImageIcon;
    }
	public JButton getButton() {
		return houseButton;
	}	
	public void ALMODE(boolean status) { //True turns ALMODE on 
		ALMODE_IS_OFF = status;
	}
}

class boardJPanel extends JPanel {
	BufferedImage image;
	boardJPanel(int x, int y) throws IOException {
		int X = x - 6;
		int Y = y - 29;
		setLayout(new GridBagLayout());
		image = ImageIO.read(new File("images/mancalaBoard.jpg"));
		Image tmp = image.getScaledInstance(X, Y, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(X, Y, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
	    image = dimg;
	}
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}
/*****End of Helper Classes*****/

/****Beginning of Main GameGUI Class****/
public class GameUI extends JPanel {
	/**Creates and manages the GUI side of Game.java
	 *NOTE: The serialVersionUID is used for version identification
	 *(Make sure that the GUI and Game are on the 'same page')
	 */
	private static final long serialVersionUID = 1; //Used for version identification (Make sure that the GUI and Game are on the 'same page')
	private int numHouses;
	private int numSeeds;
	private int playMode;
	private int x, y;
	private boolean isRandom = false;
	private Game game; 
	private JFrame window;
	private JLayeredPane boardLayers;
	private JPanel welcome, backgroundPane;
	private boardJPanel gameBoard;
	private JButton[] buttons;
	private JButton[] houseOptions;
	private JButton[] seedOptions;
    private JButton[] mancalaHouses;
    private JButton orangeCache, blueCache;
    private JButton random, begin;
    private JButton newGame,selectionMenu,options,quit;
    private JLabel playerTurn;
    private mancalaClickableHouse[] clickableHouses;
    private int houseClicked;
    private boolean updateRequired = false;
    
    ImageIcon createNewHouseImage(String iconName) {
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
    	System.out.println("Test");
    	ImageIcon resizedImageIcon = new ImageIcon(iconName);
	    Image image = resizedImageIcon.getImage(); // transform it 
	    Image newimg = image.getScaledInstance(1250, 650,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	    resizedImageIcon = new ImageIcon(newimg);  // transform it back
	    System.out.println("Test5");
	    return resizedImageIcon;
    }
    
	private ActionListener gameActions = new ActionListener()
    {
    	@Override
        public void actionPerformed(ActionEvent ae)
        {
    		for(int i = 0; i < clickableHouses.length; ++i) {
                if (ae.getSource() == clickableHouses[i].getButton())
                {
                	if(i < numHouses)
                		houseClicked = i;
                	else
                		houseClicked = i + 1;
                    updateRequired = true;
                }
    		}
            if (ae.getSource() == newGame) {
            	game.newGame(playMode, numHouses, numSeeds);
            	
            }
            else if (ae.getSource() == selectionMenu) {
            	try {
					displayWelcome();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            else if (ae.getSource() == options) {
            	//TODO
            }
            else if (ae.getSource() == quit) {
            	System.exit(0);
            }
        }
    }; 

	private ActionListener welcomeListener = new ActionListener()
	{
		@Override
	    public void actionPerformed(ActionEvent ae) {
			for(int i = 0; i < seedOptions.length; ++i) {
				if (ae.getSource() == seedOptions[i])
				{
					numSeeds = i + 1;
				}
			}
			for(int i = 0; i < houseOptions.length; ++i) {
				if (ae.getSource() == houseOptions[i])
				{
					numHouses = i + 4;
				}
			}
	        if (ae.getSource() == random)
	        {
	        	if (isRandom == true)
	        		isRandom = false;
	        	else 
	        		isRandom = true;
	        }
	        else if (ae.getSource() == begin)
	        {
	        	try {
	    			displayGUI();
	    		} catch (IOException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}
	        	game.newGame(0, numHouses, numSeeds);
	        }
			System.out.println("Number of seeds: "+ numSeeds);
		}
		
	};
	public void GUIHandler(Game _game, int _playMode, int _numHouses, int _numSeeds) throws IOException {
		this.game = _game;
		this.playMode = _playMode;
		this.numHouses = _numHouses;
		this.numSeeds = _numSeeds;
		buttons = new JButton[2*numHouses + 2];
		clickableHouses = new mancalaClickableHouse[2*numHouses];
		x = 1400;
		y = 800;
		
		//Create Window
		window = new JFrame("Mancala");
		window.setSize(x, y);
		window.setMaximumSize(new Dimension(x, y));
		window.setMinimumSize(new Dimension(1200, 600));
		window.setResizable(false);
		displayGUI();
		window.add(gameBoard);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void displayGUI() throws IOException {
		//Utilizing the GridBagLayout 
		gameBoard = new boardJPanel(x, y);
		gameBoard.setPreferredSize(new Dimension(x,y));
		GridBagConstraints c = new GridBagConstraints();
		
		//Global Constants used
		c.fill = GridBagConstraints.HORIZONTAL;
		
		/*Creating the Orange houses - The bits are put in backwards due to their orientation on the Game.java side
		*Example, 6 houses
		*0-5 blue houses, 6 blue cache
		*7-12 orange houses, 13 orange cache
		*clickableHouses 0->11
		*buttons 0->13
		*	 12	11	10	9	8	7
		*13					       6
		*	 0	1	2	3	4	5*/
		
		//Creates all the orange houses
		c.gridy = 0;
		int j = numHouses*2-1;
		for(int i = 0; i < numHouses; ++i) {
			c.gridx = i + 1;
			clickableHouses[j] = new mancalaClickableHouse(2, numSeeds);
			buttons[j+1] = clickableHouses[j].getButton();
			gameBoard.add(clickableHouses[j].getButton(), c);
			clickableHouses[j].getButton().addActionListener(gameActions);
			--j;
		}
		
		//Creating all the blue houses
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridy = 2;
		j = 0;
		for(int i = 0; i < numHouses; ++i) { //Creates all the orange houses
			c.gridx = i + 1;
			clickableHouses[j] = new mancalaClickableHouse(1, numSeeds);
			buttons[j] = clickableHouses[j].getButton();
			gameBoard.add(clickableHouses[j].getButton(), c);
			clickableHouses[j].getButton().addActionListener(gameActions);
			++j;
		}

		//Add in both user cache houses
		c.anchor = GridBagConstraints.CENTER; //Required reset due to PAGE_END
		c.gridy = 0;
		c.gridheight = 3;
		
		//Blue Cache house
		c.gridx = numHouses + 1;
		mancalaCacheHouse _blueCache = new mancalaCacheHouse(1);
		buttons[numHouses] = _blueCache.getButton();
		gameBoard.add(_blueCache.getButton(), c);

		//Orange Cache House
		c.gridx = 0;
		mancalaCacheHouse _orangeCache = new mancalaCacheHouse(2);
		buttons[2*numHouses + 1] = _orangeCache.getButton();
		gameBoard.add(_orangeCache.getButton(),c);
		
		//Game Control Buttons
		c.insets = new Insets(100,0,0,0);
		c.gridheight = 1;	//Reset height to 1
		c.gridy = 1;
		
		c.gridx = 1;
		newGame = new JButton("New Game");
		gameBoard.add(newGame, c);
		newGame.addActionListener(gameActions);
		
		c.gridx = 2;
		options = new JButton("Options");
		gameBoard.add(options, c);
		newGame.addActionListener(gameActions);
		
		if(numHouses % 2 == 0) {
			c.gridx = numHouses / 2;
			c.gridwidth = 2;
		}
		else {
			c.gridx = numHouses / 2 + 1;
			c.gridwidth = 1;
		}
		playerTurn = new JLabel("Player 1's Turn", SwingConstants.CENTER);
		gameBoard.add(playerTurn, c);
		
		c.gridwidth = 1;
		c.gridx = numHouses - 1;
		selectionMenu = new JButton("Selection Menu");
		gameBoard.add(selectionMenu, c);
		selectionMenu.addActionListener(gameActions);
		
		c.gridx = numHouses;
		quit = new JButton("Quit");
		gameBoard.add(quit, c);
		quit.addActionListener(gameActions);
	}
	
	public void displayWelcome() throws IOException {
		
		welcome = new JPanel();
		
		JLabel welcomeLabel = new JLabel("Welcome to Kalah!");
		JLabel housesLabel = new JLabel("Choose the number of houses");
		JLabel seedsLabel = new JLabel("Choose the number of seeds");
		JLabel randomLabel = new JLabel("Select if you would like the seeds to be randomized");
		
		welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 50));
		housesLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		seedsLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		randomLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		
		houseOptions = new JButton[6];
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
		welcome.add(housesLabel, c);
		
		c.gridy = 4;
		welcome.add(seedsLabel, c);
		
		c.gridy = 6;
		welcome.add(randomLabel, c);
		
		c.gridwidth = 1;
		c.gridy = 3;
		
		for (int i = 0; i < 6; i++) {
			c.gridx = i + 3;
			houseOptions[i] = new JButton(String.valueOf(i + 4));
			houseOptions[i].addActionListener(welcomeListener);
			welcome.add(houseOptions[i], c);
		}

		c.gridy = 5;
		
		for (int i = 0; i < 10; i++) {
			c.gridx = i + 1;
			seedOptions[i] = new JButton(String.valueOf(i + 1));
			seedOptions[i].addActionListener(welcomeListener);
			welcome.add(seedOptions[i], c);
		}
		
		c.gridx = 5;
		c.gridy = 7;
		random.addActionListener(welcomeListener);
		welcome.add(random, c);
		
		c.gridy = 9;
		begin.addActionListener(welcomeListener);
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
	
	void updateBoard(Game _game, int [] board) { //Called each time a Game Object is clicked
		//Update houses
		if(_game.turn == "p1")
			playerTurn.setText("Player 1's Turn");
		else
			playerTurn.setText("Player 2's Turn");
		for(int i = 0; i < board.length; ++i) {
			buttons[i].setText(Integer.toString(board[i]));
		}
	}
}
/****End of Main GameGUI Class****/
