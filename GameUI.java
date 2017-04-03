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
import java.util.Random;

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
    private JButton random, begin, playerVsPlayer, easyMode, mediumMode, hardMode;
    private JButton newGame,selectionMenu,options,quit;
    private JLabel playerTurn;
    private JLabel houses, seeds, mode;
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
            	updateRequired = true;
            }
            else if (ae.getSource() == selectionMenu) {
            	try {
            		window.remove(gameBoard);
					displayWelcome();
					window.add(welcome);
					window.validate();
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
					System.out.println("Number of seeds: "+ numSeeds);
				}
			}
			for(int i = 0; i < houseOptions.length; ++i) {
				if (ae.getSource() == houseOptions[i])
				{
					numHouses = i + 5;
					System.out.println("Number of houses: " + numHouses);
				}
			}
	        if (ae.getSource() == random)
	        {
	        	Random rand = new Random();
	        	numSeeds = rand.nextInt(9) + 1;
	        }
	        else if (ae.getSource() == begin)
	        {
	        	try {
	        		window.remove(welcome);
	        		game.newGame(playMode, numHouses, numSeeds);
	        		buttons = new JButton[2*numHouses + 2];
	        		clickableHouses = new mancalaClickableHouse[2*numHouses];
	    			displayGUI();
	    			window.add(gameBoard);
	    			window.validate();
	    		} catch (IOException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}
	        }
	        else if (ae.getSource() == playerVsPlayer)
	        {
	        	playMode = 0;
	        }
	        else if (ae.getSource() == easyMode)
	        {
	        	playMode = 1;
	        }
	        else if (ae.getSource() == mediumMode)
	        {
	        	playMode = 2;
	        }
	        else if (ae.getSource() == hardMode)
	        {
	        	playMode = 3;
	        }
	        updateWelcome();
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
		displayWelcome();
		window.add(welcome);
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
		
		welcome = new boardJPanel(x, y);
		
		JLabel welcomeLabel = new JLabel("Welcome to Kalah!", SwingConstants.CENTER);
		JLabel housesLabel = new JLabel("Choose the number of houses", SwingConstants.CENTER);
		JLabel seedsLabel = new JLabel("Choose the number of seeds", SwingConstants.CENTER);
		JLabel randomLabel = new JLabel("Select if you would like the seeds to be randomized", SwingConstants.CENTER);
		JLabel gameModeLabel = new JLabel("Choose which mode would you like to play on", SwingConstants.CENTER);
		
		welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 50));
		housesLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		seedsLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		randomLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		gameModeLabel.setFont(new Font("Serif", Font.PLAIN, 20));
		
		houseOptions = new JButton[6];
		seedOptions = new JButton[10];
		random = new JButton("Randomize");
		begin = new JButton("Begin!");
		
		welcome.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;

		//Welcome Label
		c.gridwidth = 10;
		c.gridy = 0;
		c.gridx = 0;
		welcome.add(welcomeLabel, c);
		
		//House Label
		c.gridwidth = 10;
		c.gridy = 1;
		welcome.add(housesLabel, c);
		
		//Number of Houses Selection
		c.gridwidth = 1;
		c.gridy = 2;
		for (int i = 0; i < 6; i++) {
			c.gridx = i + 2;
			houseOptions[i] = new JButton(String.valueOf(i + 5));
			houseOptions[i].addActionListener(welcomeListener);
			welcome.add(houseOptions[i], c);
		}
		
		//Seed Label
		c.insets = new Insets(10,0,0,0);
		c.gridx = 0;
		c.gridwidth = 10;
		c.gridy = 3;
		welcome.add(seedsLabel, c);
		
		//Number of Seeds Selection
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 1;
		c.gridy = 4;
		for (int i = 0; i < 10; i++) {
			c.gridx = i;
			seedOptions[i] = new JButton(String.valueOf(i + 1));
			seedOptions[i].addActionListener(welcomeListener);
			welcome.add(seedOptions[i], c);
		}
		
		//Random Label
		c.insets = new Insets(10,0,0,0);
		c.gridwidth = 10;
		c.gridx = 0;
		c.gridy = 5;
		welcome.add(randomLabel, c);
		
		//Randomize Button
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 4;
		c.gridx = 3;
		c.gridy = 6;
		random.addActionListener(welcomeListener);
		welcome.add(random, c);
		
		//Game Mode Label
		c.insets = new Insets(10,0,0,0);
		c.gridwidth = 10;
		c.gridy = 7;
		c.gridx = 0;
		welcome.add(gameModeLabel, c);
		
		//Game Mode Buttons
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 2;
		playerVsPlayer = new JButton("PvP");
		c.gridy = 8;
		c.gridx = 1;
		playerVsPlayer.addActionListener(welcomeListener);
		welcome.add(playerVsPlayer, c);
		
		easyMode = new JButton("Easy");
		c.gridy = 8;
		c.gridx = 3;
		easyMode.addActionListener(welcomeListener);
		welcome.add(easyMode, c);
		
		mediumMode = new JButton("Medium");
		c.gridy = 8;
		c.gridx = 5;
		mediumMode.addActionListener(welcomeListener);
		welcome.add(mediumMode, c);
		
		hardMode = new JButton("Hard");
		c.gridy = 8;
		c.gridx = 7;
		hardMode.addActionListener(welcomeListener);
		welcome.add(hardMode, c);
		
		//Text Feedback
		 c.insets = new Insets(20,0,0,0);
		 houses = new JLabel("Houses: " + numHouses, SwingConstants.CENTER);
		 houses.setFont(new Font("Serif", Font.PLAIN, 20));
		 c.gridwidth = 4;
		 c.gridx = 3;
		 c.gridy = 9;
		 welcome.add(houses, c);
		 
		 c.insets = new Insets(0,0,0,0);
		 seeds = new JLabel("Seeds: " + numSeeds, SwingConstants.CENTER);
		 seeds.setFont(new Font("Serif", Font.PLAIN, 20));
		 c.gridwidth = 4;
		 c.gridx = 3;
		 c.gridy = 10;
		 welcome.add(seeds, c);
		 
		 mode = new JLabel("Mode: " + getText(playMode), SwingConstants.CENTER);
		 mode.setFont(new Font("Serif", Font.PLAIN, 20));
		 c.gridwidth = 4;
		 c.gridx = 3;
		 c.gridy = 11;
		 welcome.add(mode, c);
		
		//Being Button
		c.insets = new Insets(20,0,0,0);
		c.gridwidth = 4;
		c.gridx = 3;
		c.gridy = 12;
		begin.addActionListener(welcomeListener);
		welcome.add(begin, c);
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
	
	String getText(int playerMode) {
		String returnString;
		switch(playerMode) {
			case 0:
				returnString = "PvP";
				break;
			case 1:
				returnString = "Easy";
				break;
			case 2:
				returnString = "Medium";
				break;
			case 3:
				returnString = "Hard";
				break;
			default:
				returnString = "Error";
				break;
		}
		return returnString;
	}
	
	//Called each time a Welcome Screen Object is clicked
	void updateWelcome() {
		houses.setText("Houses: " + numHouses);
		seeds.setText("Seeds: " + numSeeds);
		mode.setText("Mode: " + getText(playMode));	
	}
	
	//Called each time a Game Object is clicked
	void updateBoard(Game _game, int [] board) {
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