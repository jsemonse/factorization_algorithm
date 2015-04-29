import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
//import java.util.concurrent.ForkJoinPool;

import javax.imageio.ImageIO;

public class Main {
	private static int SIZE = 500;
	
	public static void main(String[] args) {
		// Take a path to a text file
		if(args.length > 0) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(args[0]));
				int cmd = parseCommands(br.readLine());
				PrintStream ps = new PrintStream("Output Stats for input " + args[0]);
				AllStats as = new AllStats();
				if(cmd == 3) {
					ps.println("Given edges:");
					ArrayList<Edge> e = new ArrayList<Edge>();
					// Read in the lines  and parse them to edges
					String s = br.readLine();
					while(s != null){
						Edge temp = parseToEdge(s);
						if(temp == null){
							return;
						}
						e.add(temp);
						ps.println(temp);
						s = br.readLine();
					}
					ps.println();
					// Make a grid with default initial edge and given sequence
					Grid grid = new Grid();
					grid.addInitialEdge(new Edge(new Point(0, 1, 0), new Point(1, 0, 0)));
					grid.addInitialSeq(e.toArray(new Edge[e.size()]));
					// Run algorithm and print results
					grid.runAlgorithm(1);
					as.display(new DataStructure2(grid), e.size(), 1, ps);
					drawFan(grid.lastFan(), "Final fan for input " + args[0]);
					drawFan(grid.initialFan(), "Initial fan for input " + args[0]);
					drawGridPart(grid, "Grid for input: " + args[0], new PreciseNumber(0), new PreciseNumber(0), 1);
					//drawGrid(grid, "Grid for input: " + args[0]);
				} else if(cmd > 0) {
					int distance = Integer.parseInt(br.readLine().trim());
					if(cmd == 1) {
						ps.println("Exhaustive search for "+ distance + " divisions");
						ps.println();
						exhaustiveSearch(distance, as, 1, ps);
					} else {
						ps.println("Random grid of "+ distance + " divisions");
						ps.println();
						Grid grid = solveRandGrid(distance, 1);
						as.display(new DataStructure2(grid), distance, 1, ps);
						drawFan(grid.lastFan(), "Final fan for input " + args[0]);
						drawFan(grid.initialFan(), "Initial fan for input " + args[0]);
						drawGrid(grid, "Grid for input: " + args[0]);
					}
				}
				ps.close();
				
				
			} catch (java.io.FileNotFoundException e) {
				System.out.println("File not valid");
				e.printStackTrace();
				return;
			} catch (IOException e) {
				System.out.println("Input error");
				e.printStackTrace();
				return;
			} catch (NumberFormatException e) {
				System.out.println("Invalid distance");
				e.printStackTrace();
				return;
			}
		} else {
			// Start the interactive visualizer
			@SuppressWarnings("unused")
			DrawingPanel d = new DrawingPanel();
		}
	}

	// Parse the commands
	private static int parseCommands(String s) {
		s = s.trim();
		if (s.equals("exhaustive")) {
			return 1;
		} else if (s.equals("random")) {
			return 2;
		} else if (s.equals("given")) {
			return 3;
		} else {
			System.out.println("Command parse Error");
			System.out.println("string: " + s);
			return -1;
		}
	}
	
	// Parse a string into an edge. The format of the input string must be
	// [(#,#,#),(#,#,#)]  in order to parse
	private static Edge parseToEdge(String s) {
		s = s.trim();
		String[] results = s.split("[(),]+");
		if(results.length == 8 && results[0].equals("[") && results[7].equals("]")) {
			
			try {
				int i1 = Integer.parseInt(results[1]);
				int i2 = Integer.parseInt(results[2]);
				int i3 = Integer.parseInt(results[3]);
				int j1 = Integer.parseInt(results[4]);
				int j2 = Integer.parseInt(results[5]);
				int j3 = Integer.parseInt(results[6]);
				return new Edge(new Point(i1,i2,i3),new Point(j1,j2,j3));
			} catch (NumberFormatException e) {
				System.out.println("Number parse Error");
				e.printStackTrace();
			}
		}
		System.out.println("Format parse Error");
		System.out.println("string: " + s);
		System.out.println("tokens: " + results.length);
		return null;
	}

	// This creates a random grid of specified length (up to permutation)
	// with the basic triangle as its base
	public static Grid solveRandGrid(int numSteps, int type) {
		
		if(numSteps < 0) {
			return null;
		}
		// Create the initial grid
		Grid grid = new Grid();
		// add the initial edge (the same every time to account for rotation)
		grid.addInitialEdge(new Edge(new Point(0, 1, 0), new Point(1, 0, 0)));
		// add the edge sequence (if it exists)
		if (numSteps > 0) {
			// The first edge is the same every time to account for reflection
			Edge e = new Edge(new Point(0, 0, 1), new Point(1, 0, 0));
			FanSet f = new FanSet();
			f.divideOnEdge(e);
			Edge[] edges = new Edge[numSteps];
			edges[0] = e;
			// Get the random sequence from the fan method
			System.arraycopy(f.randSeq(numSteps - 1), 0, edges, 1, numSteps - 1);
			grid.addInitialSeq(edges);
		}
		// Solve and return the grid
		grid.runAlgorithm(type);
		return grid;
	}
	
	/* This method is unused (and is incompatible with Java 6)
	// This performs a recursive exhaustive search for the specified number of steps using the specified algorithm number
	// This is done similar to map-reduce, so the provided function class must provide a way of
	// getting the value from a grid, as well as a commutative reduction operation (sum, max, etc.)
	// This variation uses a large amount of parallelism
	public static <T> void parallelSearch(int numSteps, ExhaustiveSearchReducer<T> f, int type, PrintStream ps) {
		// return a default value on bad input
	if(numSteps < 0) {
			f.display(f.initialValue(), numSteps, type, ps);
		}
		// catch the 0 case
		if (numSteps == 0) {
			// create a new Grid
			Grid grid = new Grid();
			// add the initial edge (the same every time to account for rotation)
			grid.addInitialEdge(new Edge(new Point(0, 1, 0), new Point(1, 0, 0)));
			f.display(f.getValue(grid), numSteps, type, ps);
		}
		// create a fan with the initial edge to begin generating all the possible fans
		Edge e = new Edge(new Point(0, 0, 1), new Point(1, 0, 0)); 
		FanSet fan = new FanSet();
		fan.divideOnEdge(e);
		// add the first edge to the sequence (the same every time to account for reflection)
		Edge[] edges = {e};
		// use the parallel recursive method to compute the value
		ForkJoinPool fjPool = new ForkJoinPool();
		ParallelReducer<T> recur = new ParallelReducer<T>(fan, edges, numSteps - 1, f, type);
		f.display(fjPool.invoke(recur), numSteps, type, ps);
	}
	*/

	/* This method is unused (and is incompatible with Java 6)
	// This performs a recursive exhaustive search for the specified number of steps using the specified algorithm number
	// This is done similar to map-reduce, so the provided function class must provide a way of
	// getting the value from a grid, as well as a commutative reduction operation (sum, max, etc.)
	// This variation uses a small amount of parallelism
	public static <T> void exhaustiveSearch2(int numSteps, ExhaustiveSearchReducer<T> f, int type, PrintStream ps) {
		// return a default value on bad input
		if(numSteps < 0) {
			f.display(f.initialValue(), numSteps, type, ps);
		}
		// catch the 0 case
		if (numSteps == 0) {
			// create a new Grid
			Grid grid = new Grid();
			// add the initial edge (the same every time to account for rotation)
			grid.addInitialEdge(new Edge(new Point(0, 1, 0), new Point(1, 0, 0)));
			f.display(f.getValue(grid), numSteps, type, ps);
			return;
		}
		// catch the 1 case
		if (numSteps == 1) {
			// create a new Grid
			Grid grid = new Grid();
			// add the initial edge (the same every time to account for rotation)
			grid.addInitialEdge(new Edge(new Point(0, 1, 0), new Point(1, 0, 0)));
			Edge e = new Edge(new Point(0, 0, 1), new Point(1, 0, 0));
			Edge[] edges = {e};
			grid.addInitialSeq(edges);
			grid.runAlgorithm(type);
			f.display(f.getValue(grid), numSteps, type, ps);
			return;
		}
		// create a fan with the initial edge to begin generating all the possible fans
		Edge e = new Edge(new Point(0, 0, 1), new Point(1, 0, 0)); 
		FanSet fan = new FanSet();
		fan.divideOnEdge(e);
		// add the first edge to the sequence (the same every time to account for reflection)
		Edge[] edges = {e};
		// use the recursive method to compute the value
		f.display(exhaustiveHelper2(fan, edges, numSteps - 1, f, type), numSteps, type, ps);
	}
	*/

	/* This method is unused (and is incompatible with Java 6)
		
	// This is the recursive part of the exhaustive search (it also takes the current sequence
	// and fan so it can construct the grid at the end)
	// This variation uses a small amount of parallelism
	private static <T> T exhaustiveHelper2(FanSet fan, Edge[] e, int numSteps,
			ExhaustiveSearchReducer<T> f, int type) {
		// get all the edges in the fan
		HashSet<Edge> temp = fan.getEdges();
		Edge[] edges = temp.toArray(new Edge[temp.size()]);
		// Assign the current value as the initial value
		T current = f.initialValue();
		// if there are no more steps to do, 
		if(numSteps == 1) {
			ForkJoinPool fjPool = new ForkJoinPool();
			ArrayList<ParallelEvaluator<T>> threads = new ArrayList<ParallelEvaluator<T>>();
			for(int i = 0; i < edges.length; i++) {
				// copy the current fan
				FanSet g = fan.deepCopy();
				// divide it on that edge
				g.divideOnEdge(edges[i]);
				// add the edge to the (copied) sequence
				Edge[] d = Arrays.copyOf(e, e.length + 1);
				d[e.length] = edges[i];
				// use the above in a recursive call and combine it with the current value
				ParallelEvaluator<T> recur = new ParallelEvaluator<T>(g, d, f, type);
				recur.fork();
				threads.add(recur);
			}
			for(int i = 0; i < edges.length; i++) {
				current = f.reduce(threads.get(i).join(), current);
			}
		} else {
			// iterate over all the possible edges
			for(int i = 0; i < edges.length; i++) {
				// copy the current fan
				FanSet g = fan.deepCopy();
				// divide it on that edge
				g.divideOnEdge(edges[i]);
				// add the edge to the (copied) sequence
				Edge[] d = Arrays.copyOf(e, e.length + 1);
				d[e.length] = edges[i];
				// use the above in a recursive call and combine it with the current value
				current = f.reduce(exhaustiveHelper(g, d,
						numSteps - 1, f, type), current);
			}
		}
		// return the final value
		return current;
	}
		*/

	// This performs a recursive exhaustive search for the specified number of steps using the specified algorithm number
	// This is done similar to map-reduce, so the provided function class must provide a way of
	// getting the value from a grid, as well as a commutative reduction operation (sum, max, etc.)
	public static <T> void exhaustiveSearch(int numSteps, ExhaustiveSearchReducer<T> f, int type, PrintStream ps) {
		// return a default value on bad input
		if(numSteps < 0) {
			f.display(f.initialValue(), numSteps, type, ps);
		}
		// catch the 0 case
		if (numSteps == 0) {
			// create a new Grid
			Grid grid = new Grid();
			// add the initial edge (the same every time to account for rotation)
			grid.addInitialEdge(new Edge(new Point(0, 1, 0), new Point(1, 0, 0)));
			f.display(f.getValue(grid), numSteps, type, ps);
			return;
		}
		// create a fan with the initial edge to begin generating all the possible fans
		Edge e = new Edge(new Point(0, 0, 1), new Point(1, 0, 0)); 
		FanSet fan = new FanSet();
		fan.divideOnEdge(e);
		// add the first edge to the sequence (the same every time to account for reflection)
		Edge[] edges = {e};
		// use the recursive method to compute the value
		f.display(exhaustiveHelper(fan, edges, numSteps - 1, f, type), numSteps, type, ps);
	}
	
	// This is the recursive part of the exhaustive search (it also takes the current sequence
	// and fan so it can construct the grid at the end)
	private static <T> T exhaustiveHelper(FanSet fan, Edge[] e, int numSteps,
			ExhaustiveSearchReducer<T> f, int type) {
		// if there are no more steps to do, 
		if(numSteps == 0) {
			// create a new grid
			Grid grid = new Grid();
			// add the initial edge (the same every time to account for rotation)
			grid.addInitialEdge(new Edge(new Point(0, 1, 0), new Point(1, 0, 0)));
			// add the initial sequence that has been given (the first is the same every time to
			// account for reflection)
			grid.addInitialSeq(e);
			// run the algorithm
			grid.runAlgorithm(type);
			// get the value for the grid and return it
			return f.getValue(grid);
		}
		// get all the edges in the fan
		HashSet<Edge> temp = fan.getEdges();
		Edge[] edges = temp.toArray(new Edge[temp.size()]);
		// Assign the current value as the initial value
		T current = f.initialValue();
		// iterate over all the possible edges
		for(int i = 0; i < edges.length; i++) {
			// copy the current fan
			FanSet g = fan.deepCopy();
			// divide it on that edge
			g.divideOnEdge(edges[i]);
			// add the edge to the (copied) sequence
			Edge[] d = Arrays.copyOf(e, e.length + 1);
			d[e.length] = edges[i];
			// use the above in a recursive call and combine it with the current value
			current = f.reduce(exhaustiveHelper(g, d,
					numSteps - 1, f, type), current);
		}
		// return the final value
		return current;
	}
	
	// Draws the specified grid to a JPEG with the given name
	public static void drawGrid(Grid grid, String name) {
		BufferedImage img = new BufferedImage(10*SIZE+1,
				(int)Math.ceil(10*SIZE/grid.seqLen())+1, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, 10*SIZE+1,(int)Math.ceil(10*SIZE/grid.seqLen())+1);
		grid.drawGrid(g, 10*SIZE);
		try {
			ImageIO.write(img, "jpg", new File(name + ".jpg"));
		} catch (IOException ex) {
			System.err.println(name + ".jpg could not be written");
		}
	}
	
	// Draws the part of the specified grid with the given start point scaled by the given scale to a JPEG with the given name
	public static void drawGridPart(Grid grid, String name, PreciseNumber x, PreciseNumber y, int scale) {
		BufferedImage img = new BufferedImage(10*SIZE+1,
				(int)Math.ceil(10*SIZE/grid.seqLen())+1, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.createGraphics();
		g.setColor(Color.white);
		//g.fillRect(0, 0, 10*SIZE+1, (int)Math.ceil(10*SIZE/grid.seqLen())+1);
		grid.drawGridPart(g, 10*SIZE, x, y, scale);
		try {
			ImageIO.write(img, "jpg", new File(name + ".jpg"));
		} catch (IOException ex) {
			System.err.println(name + ".jpg could not be written");
		}
	}
	
	// Draws the specified fan to a JPEG with the given name
	public static void drawFan(FanSet f, String name) {
		BufferedImage img = new BufferedImage((int)Math.ceil((double)(2 * SIZE) / Math.sqrt(3)),
				SIZE+1, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.createGraphics();
		f.drawFan(g, SIZE);
		try {
			ImageIO.write(img, "jpg", new File(name + ".jpg"));
		} catch (IOException ex) {
			System.err.println(name + ".jpg could not be written");
		}
	}
}
