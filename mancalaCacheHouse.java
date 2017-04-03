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

public class mancalaCacheHouse extends JPanel {
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