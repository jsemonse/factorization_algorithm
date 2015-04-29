import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class FanPanel extends JPanel{
	private static final int SIZE = 500;
	private static final int WIDTH = (int)Math.ceil((double)(2 * SIZE) / Math.sqrt(3));
	// The grid point for the window
	private GridPoint gp;
	
	// Make a display panel for the given grid point
	public FanPanel(Frame f, GridPoint gp) {
		// Set the internal variables
		this.gp = gp;
		JDialog frame = new JDialog(f, "Fan (" + gp.getX().approxValue() + ", " + gp.getY().approxValue() +")");
		frame.setLayout(new BorderLayout());
		
		setBackground(Color.white);
		frame.setVisible(true);
		
		// Add buttons
		frame.add(this, BorderLayout.CENTER);
		JButton printButton = new JButton("Print");
		
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawFan();
				printStats();
			}
		});
		frame.add(printButton, BorderLayout.SOUTH);
		
		// Build and display window
		setPreferredSize(new Dimension(WIDTH, SIZE+1));
		repaint();
		frame.pack();
	}
	
	// Display the fan and distribution of generators
	public void paint(Graphics g) {
		gp.getFan().drawFan(g, SIZE);
		int[] list = gp.getFan().getPointsDists();
		g.drawString("Distribution of distances:", WIDTH - 160, 15);
		for(int i = 0; i < list.length; i += 3) {
			String s = "";
			for(int j = 0; j < Math.min(3, list.length - i); j++) {
				int len = i + j + 1;
				s = s + len + ": " + list[i + j] + " ";
			}
			g.drawString(s, WIDTH - 120, 4 * i + 30);
		}
	}
	
	// Draw the fan for a given grid point to a new jpeg
	private void drawFan() {
		BufferedImage img = new BufferedImage(WIDTH,
				SIZE+1, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.createGraphics();
		gp.getFan().drawFan(g, SIZE);
		try {
			ImageIO.write(img, "jpg", new File("Fan (" + gp.getX().approxValue() + ", " + gp.getY().approxValue() +").jpg"));
		} catch (IOException ex) {
			System.err.println("Fan (" + gp.getX().approxValue() + ", " + gp.getY().approxValue() +").jpg could not be written");
		}
	}
	
	// This prints the statistics for the grid point to a new text file
	private void printStats() {
		try {
			PrintStream ps = new PrintStream("Output Stats for Fan (" + gp.getX().approxValue() + ", " + gp.getY().approxValue() +").txt");
			ps.println("Distribution of distances:");
			int[] list = gp.getFan().getPointsDists();
			for(int i = 0; i < list.length; i++) {
				ps.println((i+1) + ": " + list[i]);
			}
			ps.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
