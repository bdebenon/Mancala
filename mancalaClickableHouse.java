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

public class mancalaClickableHouse extends JPanel {
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