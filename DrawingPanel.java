import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class DrawingPanel extends JPanel{
	private static final int SIZE = 500;
	// The panel in which to display the grid
	private JFrame frame; 
	// The grid to be displayed
	private Grid grid;
	// The sequence of edges that amke up the grid
	private ArrayList<Edge> edges;
	// The level of zoom on the grid display
	private int zoomLevel;
	// Whether there is a new input sequence.
	private boolean newInput;
	
	public DrawingPanel() {
		// Set initial values
		newInput = true;
		zoomLevel = 0;
		resetEdges();
		
		// Build frame
		frame = new JFrame("Factorization Algorithm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		// Add interactive menu
		JMenuBar mb = new JMenuBar();
		frame.setJMenuBar(mb);
		JMenu fileMenu = new JMenu("File");
		mb.add(fileMenu);
		JMenuItem newFan = new JMenuItem("New Fan");
		JMenuItem export = new JMenuItem("Export Grid");
		fileMenu.add(newFan);
		fileMenu.add(export);
		
		newFan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetEdges();
				newInput = true;
			}
		});
		
		export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PrintStream ps = new PrintStream("Initial Edges.txt");
					ps.println("given");
					for(int i = 0; i < edges.size(); i++) {
						ps.println(edges.get(i).toString());
					}
					ps.close();
				} catch (FileNotFoundException exeption) {
					// TODO Auto-generated catch block
					exeption.printStackTrace();
				}
			}
		});
		
		// create display window for grid
		setBackground(Color.white);
		JScrollPane scroll = new JScrollPane(this,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(2*SIZE+10, SIZE+10));
		frame.add(scroll, BorderLayout.CENTER);
		
		// Add interactive buttons
		JButton zoomIn = new JButton("Zoom In");
		JButton zoomOut = new JButton("Zoom Out");
		
		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomLevel++;
				repaint();
				//System.out.println("button was pushed!");
			}
		});
		
		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(zoomLevel > 0) {
					zoomLevel--;
				}
				repaint();
				//System.out.println("button was pushed!");
			}
		});
		
		frame.add(zoomIn, BorderLayout.NORTH);
		frame.add(zoomOut, BorderLayout.SOUTH);
		
		JButton addEdge = new JButton("Add Edge");
		JButton removeEdge = new JButton("Remove Edge");
		
		addEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						newInput = true;
						//addGrid();
					}
				});
			}
		});
		
		removeEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(edges.size() > 1) {
					edges.remove(edges.size()-1);
					rebuildGrid();
					repaint();
				}
			}
		});
		
		frame.add(addEdge, BorderLayout.EAST);
		frame.add(removeEdge, BorderLayout.WEST);
		
		// Add mouse interactivity
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				double x = ((double)grid.seqLen() * (double)e.getY() / (double)drawSize());
				double y = ((double)grid.seqLen() * (double)e.getX() / (double)drawSize());
				
				//System.out.println("X coordinate:" + x);
				//System.out.println("Y coordinate:" + y);
				
				new FanPanel(frame, grid.getNearestGridpoint(x, y));
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
		
		// Build window
		frame.pack();
		
		// Update grid when needed
		while(true) {
			while(!newInput) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			newInput = false;
			addGrid();
		}
	}
	
	// Draw grid
	public void paint(Graphics g) {
		setPreferredSize(new Dimension(drawSize(), (int)(drawSize()/grid.seqLen() + 1)));
		grid.drawGrid(g, drawSize());
		
	}
	
	// Compute size for zoom level
	private int drawSize() {
		return (int)(SIZE*Math.pow(2, zoomLevel));
	}
	
	// Make a grid via an input pane
	private void addGrid() {
		frame.setVisible(false);
		InputPanel input = new InputPanel(frame, edges);
		while(input.isShowing()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		edges = input.getEdges();

		rebuildGrid();
		
		frame.setVisible(true);
		repaint();
	}
	
	// Reset the edge list
	private void resetEdges() {
		edges = new ArrayList<Edge>();
		edges.add(new Edge(new Point(0, 0, 1), new Point(1, 0, 0)));
	}
	
	// rebuilds grid from sequence
	private void rebuildGrid() {
		grid = new Grid();
		grid.addInitialEdge(new Edge(new Point(0, 1, 0), new Point(1, 0, 0)));
		grid.addInitialSeq(edges.toArray(new Edge[edges.size()]));
		grid.runAlgorithm(1);
	}

}
