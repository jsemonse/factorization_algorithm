import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class InputPanel extends JPanel{
	private static final int SIZE = 500;
	// The current fan on which we can divide
	private FanSet fan;
	// The currently highlighted edge
	private Edge current;
	// The current sequence of edges
	private ArrayList<Edge> edges;
	
	// Takes as input a frame in which to display, and an initial set of edges on which to divide.
	public InputPanel(Frame f, ArrayList<Edge> array) {
		// Initialize fan
		fan = new FanSet();
		for(int i = 0; i < array.size(); i++) {
			fan.divideOnEdge(array.get(i));
		}
		// Initialize other variables
		edges = array;
		current = null;
		JDialog frame = new JDialog(f, "New Fan Creator");
		
		setBackground(Color.white);
		frame.setVisible(true);
		
		// Make the window
		frame.add(this);
		setPreferredSize(new Dimension((int)Math.ceil((double)(2 * SIZE) / Math.sqrt(3)), SIZE+10));
		
		// Add mouse interactivity
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				double x = (double)e.getX()/(double)SIZE;
				double y = (double)e.getY()/(double)SIZE;
				Edge edge = fan.nearestEdge(x, y);
				if(edge != null && !edge.equals(current)) {
					current = edge;
					repaint();
				}
			}
			
		});
		
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(current != null) {
					fan.divideOnEdge(current);
					edges.add(current);
					current = null;
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		frame.pack();
		//System.out.println("frame packed");
		repaint();
		//paintImmediately(0,0, (int)Math.ceil((double)(2 * SIZE) / Math.sqrt(3)), SIZE+10);

		
		System.out.println("done");
	}
	
	// Draw the fan
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println("begin draw");
		fan.drawFan(g, SIZE);
		//System.out.println("fan drawn");
		if(current != null) {
			g.setColor(Color.red);
			current.drawEdge(g, SIZE);
			g.setColor(Color.black);
		}
		//System.out.println("draw done");
	}
	
	// Get the sequence of edges
	public ArrayList<Edge> getEdges() {
		return edges;
	}
}