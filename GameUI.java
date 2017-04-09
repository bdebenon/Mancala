import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class GameUI extends JPanel implements Runnable {
	/**Creates and manages the GUI side of Game.java
	 *NOTE: The serialVersionUID is used for version identification
	 *(Make sure that the GUI and Game are on the 'same page')
	 */
	private static final long serialVersionUID = 1; //Used for version identification (Make sure that the GUI and Game are on the 'same page')
	private int numHouses;
	private int numSeeds;
	private int playMode;
	private int x, y; 
	static private String turn;
	final private JFrame window;
	private JPanel welcome, winnerScreen, aboutPanel;
	private boardJPanel gameBoard;
	private JButton[] buttons;
	private JButton[] houseOptions;
	private JButton[] seedOptions;
	private JButton random, begin, playerVsPlayer, easyMode, mediumMode, hardMode;
    private JButton newGame,selectionMenu,about,quit, playAgain, returnToGame;
    private JLabel playerTurn;
    private JLabel houses, seeds, mode, winner;
    private mancalaClickableHouse[] clickableHouses;
    private int houseClicked;
	private boolean isRandom;
    private final BlockingQueue<String> informationQueueIn;
    private final BlockingQueue<String> informationQueueOut;
    
    public GameUI (BlockingQueue<String> _informationQueueIn, BlockingQueue<String> _informationQueueOut) throws IOException {
    	informationQueueIn = _informationQueueIn;
    	informationQueueOut = _informationQueueOut;
		x = 1400;
		y = 800;
		numSeeds = 4;
		numHouses = 6;
		playMode = 0;
		turn = "p1";
		window = new JFrame("Mancala");
	    	createAbout();
    }
    
	private ActionListener gameActions = new ActionListener()
    {
    	@Override
        public void actionPerformed(ActionEvent ae)
        {
    		try {
	    		for(int i = 0; i < clickableHouses.length; ++i) {
	                if (ae.getSource() == clickableHouses[i].getButton())
	                {
	                	if(i < numHouses)
	                		houseClicked = i;
	                	else
	                		houseClicked = i + 1;
	                    String move = "MOVE_NULL_" + houseClicked;
						informationQueueOut.put(move);
	                }
	    		}
	            if (ae.getSource() == newGame) {
	            	newGame();
	            }
	            else if (ae.getSource() == selectionMenu) {
            		window.remove(gameBoard);
					createWelcome();
					window.add(welcome);
					window.validate();
					informationQueueOut.put("SELECTION");
	            }
	            else if (ae.getSource() == about) {
	            	window.remove(gameBoard);
			window.setContentPane(aboutPanel);
			window.invalidate();
			window.validate();
	            	informationQueueOut.put("ABOUT");
	            }
	            else if (ae.getSource() == quit) {
	            	informationQueueOut.put("EXIT");
	            	System.exit(0);
	            }
	        } catch (IOException | InterruptedException e1) {
	        	e1.printStackTrace();
	        }
        }
    }; 

	private ActionListener welcomeListener = new ActionListener()
	{
		@Override
	    public void actionPerformed(ActionEvent ae) {
			try {
				for(int i = 0; i < seedOptions.length; ++i) {
					if (ae.getSource() == seedOptions[i])
					{
						numSeeds = i + 1;
						//System.out.println("Number of seeds: "+ numSeeds);
					}
				}
				for(int i = 0; i < houseOptions.length; ++i) {
					if (ae.getSource() == houseOptions[i])
					{
						numHouses = i + 4;
						if(numHouses > 6)
							x = 1750;
						else
							x = 1400;
						//System.out.println("Number of houses: " + numHouses);
					}
				}
		        if (ae.getSource() == random)
		        {
		        	if (isRandom == false) {
		        		isRandom = true;
		        	}
		        	else {
		        		isRandom = false;
		        	}
		        }
		        else if (ae.getSource() == begin)
		        {
		        	begin();
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
			} catch (IOException | InterruptedException e1) {
	        	e1.printStackTrace();
			}
		}
	};
	
	private ActionListener winnerListener = new ActionListener()
	{
		@Override
	    public void actionPerformed(ActionEvent ae) {
		try {
			if (ae.getSource() == playAgain) {
				//window.remove(winnerScreen);
				restart();
			}
		} catch (IOException | InterruptedException e1) {
		e1.printStackTrace();
		}
		}
	};
	
	private ActionListener aboutListener = new ActionListener()
	{
		@Override
	    public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == returnToGame) {
				System.out.println("remove about panel");
				//window.remove(aboutPanel);
				window.setContentPane(gameBoard);
				window.invalidate();
				window.validate();
			}
		}
	};

	void restart() throws InterruptedException, IOException {
		window.remove(winnerScreen);

		createGameBoard();
		String move = "ACK_GAMEINFO_" + playMode + "_" + numHouses + "_" + numSeeds + "_" + booleanStatus;
		informationQueueOut.put(move);
		window.add(gameBoard);
		window.validate();
	}	
	void begin() throws InterruptedException, IOException {
		window.remove(welcome);
		createGameBoard();
		String move = "ACK_GAMEINFO_" + playMode + "_" + numHouses + "_" + numSeeds + "_" + booleanStatus;
		informationQueueOut.put(move);
		window.add(gameBoard);
		window.validate();
	}
	
	void newGame() throws InterruptedException {
		String move = "ACK_GAMEINFO_" + playMode + "_" + numHouses + "_" + numSeeds + "_" + booleanStatus;
		informationQueueOut.put(move);
	}
	
	public void createWindow() throws IOException {
		//Create Window
		window.setSize(x, y);
		createWelcome();
		window.setVisible(true);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void createGameBoard() throws IOException {
		window.setSize(x, y);
		gameBoard = new boardJPanel(x, y);
		//Utilizing the GridBagLayout 
		buttons = new JButton[2*numHouses + 2];
		clickableHouses = new mancalaClickableHouse[2*numHouses];
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
		if(numHouses != 4) {
			for(int i = 0; i < numHouses; ++i) {
				c.gridx = i + 1;
				clickableHouses[j] = new mancalaClickableHouse(2, numSeeds);
				buttons[j+1] = clickableHouses[j].getButton();
				gameBoard.add(clickableHouses[j].getButton(), c);
				clickableHouses[j].getButton().addActionListener(gameActions);
				--j;
			}
		} else { //Fixes odd alignment of 4
			for(int i = 0; i < numHouses; ++i) {
				if(i <= 1)
					c.gridx = i + 1;
				else
					c.gridx = i + 2;
				clickableHouses[j] = new mancalaClickableHouse(2, numSeeds);
				buttons[j+1] = clickableHouses[j].getButton();
				gameBoard.add(clickableHouses[j].getButton(), c);
				clickableHouses[j].getButton().addActionListener(gameActions);
				--j;
			}
		}
		
		//Creating all the blue houses
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridy = 2;
		j = 0;
		if(numHouses != 4) {
			for(int i = 0; i < numHouses; ++i) { //Creates all the orange houses
				c.gridx = i + 1;
				clickableHouses[j] = new mancalaClickableHouse(1, numSeeds);
				buttons[j] = clickableHouses[j].getButton();
				gameBoard.add(clickableHouses[j].getButton(), c);
				clickableHouses[j].getButton().addActionListener(gameActions);
				++j;
			}
		} else {
			for(int i = 0; i < numHouses; ++i) { //Fixes odd alignment of 4
				if(i <= 1)
					c.gridx = i + 1;
				else
					c.gridx = i + 2;
				clickableHouses[j] = new mancalaClickableHouse(1, numSeeds);
				buttons[j] = clickableHouses[j].getButton();
				gameBoard.add(clickableHouses[j].getButton(), c);
				clickableHouses[j].getButton().addActionListener(gameActions);
				++j;
			}
		}

		//Add in both user cache houses
		c.anchor = GridBagConstraints.CENTER; //Required reset due to PAGE_END
		c.gridy = 0;
		c.gridheight = 3;
		
		//Blue Cache house
		if(numHouses != 4)
			c.gridx = numHouses + 1;
		else
			c.gridx = numHouses + 2;
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
		about = new JButton("About");
		gameBoard.add(about, c);
		about.addActionListener(gameActions);
		
		if(numHouses % 2 == 0) {
			c.gridx = numHouses / 2;
			c.gridwidth = 2;
		}
		else {
			c.gridx = numHouses / 2 + 1;
			c.gridwidth = 1;
		}
		if(numHouses == 4) {
			c.gridx = numHouses / 2 + 1;
			c.gridwidth = 1;
			c.insets = new Insets(100,30,0,30);
		}
		playerTurn = new JLabel("Player 1's Turn", SwingConstants.CENTER);
		gameBoard.add(playerTurn, c);
		
		c.insets = new Insets(100,0,0,0);
		c.gridwidth = 1;
		c.gridx = numHouses - 1;
		if(numHouses == 4)
			c.gridx = numHouses;
		selectionMenu = new JButton("Selection Menu");
		gameBoard.add(selectionMenu, c);
		selectionMenu.addActionListener(gameActions);
		
		c.gridx = numHouses;
		if(numHouses == 4)
			c.gridx = numHouses + 1;
		quit = new JButton("Quit");
		gameBoard.add(quit, c);
		quit.addActionListener(gameActions);
	}
	
	public void createWelcome() throws IOException {
		window.setSize(x, y);
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
			houseOptions[i] = new JButton(String.valueOf(i + 4));
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
		
		//Begin Button
		c.insets = new Insets(20,0,0,0);
		c.gridwidth = 4;
		c.gridx = 3;
		c.gridy = 12;
		begin.addActionListener(welcomeListener);
		welcome.add(begin, c);
	}
	
	public void createWinScreen(int winStatus) throws IOException {
		//0 -> Win, 1 -> LOSE, 2 -> TIE
		JLabel winner = new JLabel();
		window.setSize(x, y);
		winnerScreen = new boardJPanel(x, y);
		if (winStatus == 0) {
			winner.setText("You win!");
		}
		else if (winStatus == 1) {
			winner.setText("You lose!");
		}
		else if (winStatus == 2) {
			winner.setText("It's a tie!");
		}
		
		winner.setFont(new Font("Serif", Font.PLAIN, 50));
		playAgain = new JButton("Play Again");
		playAgain.addActionListener(winnerListener);
		
		winnerScreen.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		
		c.gridy = 2;
		winnerScreen.add(winner, c);
		
		c.gridy = 4;
		winnerScreen.add(playAgain, c);
		
		System.out.println("Winner Status: " + winStatus);
	}
	
	void createAbout() throws IOException {
		window.setSize(x, y);
		aboutPanel = new boardJPanel(x, y);
		returnToGame = new JButton("Return to Game");
		aboutPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 1;
		
		JLabel abt = new JLabel("About Kalah");
		abt.setFont(new Font("Serif", Font.PLAIN, 50));
		aboutPanel.add(abt, c);

		c.gridy++;
		BufferedReader in = new BufferedReader(new FileReader("about.txt"));
		String line;
		
		while ((line = in.readLine()) != null) {
			aboutPanel.add(new JLabel("<html>" + line + "</html>"), c);
			c.gridy++;
		}
		in.close();
		
		returnToGame.addActionListener(aboutListener);
		aboutPanel.add(returnToGame, c);	
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
	void updateBoard(String [] board, String _playerTurn) {
		if(_playerTurn.equalsIgnoreCase("p1")) {
			playerTurn.setText("Player 1's Turn");
			turn = "p1";
		} else if (_playerTurn.equalsIgnoreCase("p2")) {
			playerTurn.setText("Player 2's Turn");
			turn = "p2";
		} else {
			playerTurn.setText("Thinking...");
		}
		for(int i = 0; i < board.length; ++i) {
			buttons[i].setText(board[i]);
		}
	}
	
	public void run() {
		try {
			informationQueueOut.put("ACK_READY");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			String [] info;
			try {
				info = (informationQueueIn.take()).split("_");
				System.out.println("Command Recieved: " + info[0]);
				switch(info[0]) {
				case "INFO": //Start Game
					//TODO
					break;
				case "MOVE": //Updated Board Sent With No Turn Change
					String _playerTurn = info[1];
					String [] board = new String [info.length-2];
					for(int i = 2; i < info.length; ++i) {
						board[i-2] = info[i];
					}
					updateBoard(board, _playerTurn);
					break;
				case "ACK":
					System.out.println("SubCommand Recieved: " + info[1]);
					switch(info[1]) {
					case "BEGIN":
						//TODO
						createWindow();
						informationQueueOut.put("ACK_OK");
						break;
					case "WELCOME":
						//TODO
	            		//window.remove(gameBoard);
						window.add(welcome);
						window.validate();
						break;
					case "ILLEGAL":
						if(turn.equalsIgnoreCase("p1")) {
							playerTurn.setText("<html><span>Player 1's Turn<br>ILLEGAL MOVE</span></html>");
							//TODO make this go on two lines
							turn = "p1";
						} else if (turn.equalsIgnoreCase("p2")) {
							playerTurn.setText("<html><span>Player 2's Turn<br>ILLEGAL MOVE</span></html>");
							turn = "p2";
						}
						break;
					case "TIME":
						//TODO
						break;
					case "WINNER":
						createWinScreen(0);
						window.add(winnerScreen);
						window.validate();
						break;
					case "LOSER":
						createWinScreen(1);
						window.add(winnerScreen);
						window.validate();
						break;
					case "TIE":
						createWinScreen(2);
						window.add(winnerScreen);
						window.validate();
						break;
					case "OK":
						//TODO
						break;
					}
					break;
				}
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
