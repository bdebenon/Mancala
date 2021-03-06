import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class boardJPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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