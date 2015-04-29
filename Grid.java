import java.awt.Graphics;
import java.util.TreeSet;
import java.util.Iterator;

// This class represents an entire grid of points, showing the entire path of the algorithm
public class Grid {
	// This is the start point (only really needed for reference when setting up the initial edges)
	private GridPoint start;
	// And this is the set of points in the grid
	private TreeSet<GridPoint> points;
	// This is the length of the edge sequence in the grid
	private int seqLen;
	
	// This creates an empty grid save for the start point which is the given point in the grid
	public Grid(GridPoint gp) {
		start = gp;
		points = new TreeSet<GridPoint>(new GridComparator());
		points.add(gp);
		seqLen = 0;
	}
	
	// This creates a grid with a default point in the grid as its start point
	public Grid() {
		start = new GridPoint();
		points = new TreeSet<GridPoint>(new GridComparator());
		points.add(start);
		seqLen = 0;
	}
	
	// This adds the initial sequence of subdivisions to the grid, denoted by edges
	// these divisions go in the y direction and determines the width of the grid
	public boolean addInitialSeq(Edge[] init) {
		GridPoint gp = start;
		for(int i = 0; i < init.length; i++) {
			if(init[i] == null || gp.getFan().getEdges().contains(init[i])) {
				gp.setYEdge(gp.getFan().midToEdge(init[i].midpoint()));
				FanSet f = gp.getFan().deepCopy();
				f.divideOnEdge(init[i]);
				GridPoint newPt = new GridPoint(f, 0, i+1);
				gp.setYNext(newPt);
				points.add(newPt);
				/*
				if(points.add(newPt)) {
					//System.out.println("Added (" + newPt.getX().toString() + ", "+ newPt.getY().toString() + ") to grid");
				} else {
					//System.out.println("(" + newPt.getX().toString() + ", "+ newPt.getY().toString() + ") already in grid");
				}*/
				gp = newPt;
			} else {
				return false;
			}
		}
		seqLen = init.length;
		return true;
	}
	
	// This adds the initial subdivision that is commuted with the sequence to the grid,
	// denoted by an edge. This goes in the x direction
	public boolean addInitialEdge(Edge e) {
		if(e == null || start.getFan().getEdges().contains(e)) {
			start.setXEdge(start.getFan().midToEdge(e.midpoint()));
			FanSet f = start.getFan().deepCopy();
			f.divideOnEdge(e);
			GridPoint gp = new GridPoint(f, 1, 0);
			start.setXNext(gp);
			points.add(gp);
			//System.out.println("Added (~1.0, ~0.0) to grid");
			return true;
		} else {
			return false;
		}
	}
	
	// This finds the next point that is ready to be processed by the algorithm (order is
	// implementation dependent (used in type 2 & 3 algorithms) and consistent order is not needed)
	public GridPoint nextForAlgorithm() {
		Iterator<GridPoint> itr = points.iterator();
		GridPoint gp = null;
		while(itr.hasNext()) {
			gp = itr.next();
			if(gp.toDivide()) {
				return gp;
			}
		}
		return null;
	}
	
	// This processed the given point in the grid, returning if it succeeded in finding a way to
	// reconcile the two subdivisions in either direction using the given algorithm number
	public boolean processPoint(GridPoint gp, int alg) {
		// fail if not ready for processing
		if(!gp.toDivide()) {
			return false;
		}
		//System.out.println("Processing (" + gp.getX().toString() + ", "+ gp.getY().toString() + ")");
		// collect important info about the point for future use
		int a = gp.divisionType(alg);
		Edge xEdge = gp.getXEdge();
		Edge yEdge = gp.getYEdge();
		GridPoint gpX = gp.getXNext();
		GridPoint gpY = gp.getYNext();
		// for type 1, create a new point that simply is a null edge away from each of the next points
		// to approximate a triangle with null edges
		if(a == 1) {
			GridPoint newGp = new GridPoint(gpX.getFan().deepCopy(),
					gpX.getX(), gpY.getY());
			gpX.setYNext(newGp);
			gpX.setYEdge(null);
			gpY.setXNext(newGp);
			gpY.setXEdge(null);
			points.add(newGp);
			//System.out.println("Added (" + newGp.getX().toString() + ", "+ newGp.getY().toString() + ") to grid");
		// for type 2, simply commute the edges, enacting the same divisions in 2 different ways
		} else if (a == 2) {
			FanSet f = gpX.getFan().deepCopy();
			f.divideOnEdge(yEdge);
			GridPoint newGp = new GridPoint(f, gpX.getX(), gpY.getY());
			gpX.setYNext(newGp);
			gpX.setYEdge(yEdge);
			gpY.setXNext(newGp);
			gpY.setXEdge(xEdge);
			points.add(newGp);
			//System.out.println("Added (" + newGp.getX().toString() + ", "+ newGp.getY().toString() + ") to grid");
		// for type 3, create the intermediate steps by commuting edges, then dividing on the middle
		// point to reconcile the differences. The intermediate steps go halfway in between the originals
		// and the newly created reconciliation.
		} else if (a == 3) {
			Point [] xpoints = xEdge.getPoints();
			Point [] ypoints = yEdge.getPoints();
			Point z;
			if(xEdge.hasPoint(ypoints[1])) {
				z = new Point(xpoints[0].getX() + xpoints[1].getX() + ypoints[0].getX(),
						xpoints[0].getY() + xpoints[1].getY() + ypoints[0].getY(),
						xpoints[0].getZ() + xpoints[1].getZ() + ypoints[0].getZ());
			} else {
				z = new Point(xpoints[0].getX() + xpoints[1].getX() + ypoints[1].getX(),
						xpoints[0].getY() + xpoints[1].getY() + ypoints[1].getY(),
						xpoints[0].getZ() + xpoints[1].getZ() + ypoints[1].getZ());
			}
			
			
			
			FanSet f = gpX.getFan().deepCopy();
			f.divideOnEdge(yEdge);
			GridPoint newGpX = new GridPoint(f, gpX.getX(), PreciseNumber.average(gp.getY(), gpY.getY()));
			gpX.setYNext(newGpX);
			gpX.setYEdge(yEdge);
			points.add(newGpX);
			//System.out.println("Added (" + newGpX.getX().toString() + ", "+ newGpX.getY().toString() + ") to grid");
			
			FanSet g = gpY.getFan().deepCopy();
			g.divideOnEdge(xEdge);
			GridPoint newGpY = new GridPoint(g, PreciseNumber.average(gp.getX(), gpX.getX()), gpY.getY());
			gpY.setXNext(newGpY);
			gpY.setXEdge(xEdge);
			points.add(newGpY);
			//System.out.println("Added (" + newGpY.getX().toString() + ", "+ newGpY.getY().toString() + ") to grid");

			FanSet h = f.deepCopy();
			h.divideOnEdge(h.midToEdge(z));
			GridPoint newGp = new GridPoint(h, gpX.getX(), gpY.getY());
			newGpX.setYNext(newGp);
			newGpX.setYEdge(f.midToEdge(z));
			newGpY.setXNext(newGp);
			newGpY.setXEdge(g.midToEdge(z));
			points.add(newGp);
			//System.out.println("Added (" + newGp.getX().toString() + ", "+ newGp.getY().toString() + ") to grid");
		} else {
			// for type 4, create the intermediate steps by dividing on the middle point, then commuting
			// edges to reconcile the differences. The intermediate steps go halfway in between the originals
			// and the newly created reconciliation.
			Point [] xpoints = xEdge.getPoints();
			Point [] ypoints = yEdge.getPoints();
			Point z;
			if(xEdge.hasPoint(ypoints[1])) {
				z = new Point(xpoints[0].getX() + xpoints[1].getX() + ypoints[0].getX(),
						xpoints[0].getY() + xpoints[1].getY() + ypoints[0].getY(),
						xpoints[0].getZ() + xpoints[1].getZ() + ypoints[0].getZ());
			} else {
				z = new Point(xpoints[0].getX() + xpoints[1].getX() + ypoints[1].getX(),
						xpoints[0].getY() + xpoints[1].getY() + ypoints[1].getY(),
						xpoints[0].getZ() + xpoints[1].getZ() + ypoints[1].getZ());
			}
			
			
			
			FanSet f = gpX.getFan().deepCopy();
			Edge newYedge = f.midToEdge(z);
			f.divideOnEdge(newYedge);
			GridPoint newGpX = new GridPoint(f, gpX.getX(), PreciseNumber.average(gp.getY(),gpY.getY()));
			gpX.setYNext(newGpX);
			gpX.setYEdge(newYedge);
			points.add(newGpX);
			//System.out.println("Added (" + newGpX.getX().toString() + ", "+ newGpX.getY().toString() + ") to grid");
			
			FanSet g = gpY.getFan().deepCopy();
			Edge newXedge = g.midToEdge(z);
			g.divideOnEdge(newXedge);
			GridPoint newGpY = new GridPoint(g, PreciseNumber.average(gp.getX(), gpX.getX()), gpY.getY());
			gpY.setXNext(newGpY);
			gpY.setXEdge(newXedge);
			points.add(newGpY);
			//System.out.println("Added (" + newGpY.getX().toString() + ", "+ newGpY.getY().toString() + ") to grid");

			FanSet h = f.deepCopy();
			h.divideOnEdge(yEdge);
			GridPoint newGp = new GridPoint(h, gpX.getX(), gpY.getY());
			newGpX.setYNext(newGp);
			newGpX.setYEdge(yEdge);
			newGpY.setXNext(newGp);
			newGpY.setXEdge(xEdge);
			points.add(newGp);
			//System.out.println("Added (" + newGp.getX().toString() + ", "+ newGp.getY().toString() + ") to grid");
		}
		return true;
	}
	
	// This runs the specified type of algorithm iteratively until the algorithm breaks or is done & returns if it succeeded
	public boolean runAlgorithm(int type) {
		boolean bool = true;
		while(bool) {
			GridPoint gp = nextForAlgorithm();
			bool = (gp != null) && processPoint(gp, type);
			if(gp != null) {
				//System.out.println(toString());
			}
		}
		return nextForAlgorithm() == null;
	}
	
	// This returns a text representation of the grid, printing all the points
	// the order is determined by implementation, and could be changed
	public String toString() {
		String str = "";
		Iterator<GridPoint> itr = points.iterator();
		while(itr.hasNext()) {
			str += itr.next().toString() + "\n";
		}
		return str;
	}
	
	// This returns the number of points in the fan at the end of the algorithm
	public int numPoints() {
		return lastFan().numPoints();
	}
	
	// This returns the number of edges in the fan at the end of the algorithm
	public int numEdges() {
		return lastFan().numEdges();
	}
	
	// This returns the number of triangles in the fan at the end of the algorithm
	public int numTriangles() {
		return lastFan().numTriangles();
	}
	
	// This returns the number of grid points in the grid after the algorithm
	public int numGridPoints() {
		return points.size();
	}
	
	// This gets the farthest point in the fan at the end of the algorithm
	public int farthestLength() {
		return lastFan().getLongestLength();
	}
	
	// This gets the farthest old point in the fan
	public int farthestOldLength() {
		return initialFan().getLongestIntersectionLength(start.getFan().getNewEdges(start.getXEdge()));
	}
	
	// This returns the number of type 1 and type 3 divisions
	public int[] divisionTypes() {
		int[] returned = {0,-1};
		GridPoint current = start;
		while(current.getYNext() != null) {
			current = current.getYNext();
		}
		while(current.getXNext() != null) {
			if(current.getXEdge() == null) {
				returned[0] += 1;
			}
			returned[1] += 1;
			current = current.getXNext();
		}
		return returned;
	}
	
	// This gets the last fan at the end of the algorithm
	public FanSet lastFan() {
		GridPoint current = start;
		while(current.getXNext() != null) {
			current = current.getXNext();
		}
		while(current.getYNext() != null) {
			current = current.getYNext();
		}
		return current.getFan();
	}
	
	// This gets the initial fan (of multiple steps) that is merged with the simple one
	public FanSet initialFan() {
		GridPoint current = start;
		while(current.getYNext() != null) {
			current = current.getYNext();
		}
		return current.getFan();
	}
	
	// this gets the initial sequence of edges that create the complex fan at the beginning of the algorithm
	public Edge[] getInitialSeq() {
		Edge[] edges = new Edge[seqLen];
		//Edge[] edges = new Edge[(int)points.last().getY()];
		GridPoint current = start;
		for(int i = 0; i < edges.length; i++) {
			edges[i] = current.getYEdge();
			current = current.getYNext();
		}
		return edges;
	}
	
	// gets a list of distances of the points in the result fan
	// with associated counts
	public int[] getPointsDists() {
		return lastFan().getPointsDists();
	}
	
	// gets the deepest grid coordinate, where deepest means the highest precision x or y coordinate
	public int getDeepestGridCoord() {
		int depth = 0;
		Iterator<GridPoint> pitr = points.iterator();
		while(pitr.hasNext()) {
			GridPoint gp = pitr.next();
			depth = Math.max(depth, Math.max(gp.getX().getLength(), gp.getY().getLength()));
		}
		return depth;
	}
	
	// Draw the grid on the given graphics at the given size
	public void drawGrid(Graphics g, int size) {
		Iterator<GridPoint> pitr = points.iterator();
		while(pitr.hasNext()) {
			GridPoint gp = pitr.next();
			gp.drawGrid(g, size/seqLen, start.getX(), start.getY());
		}
	}
	
	// Get the length of the sequence that makes this grid
	public int seqLen() {
		return seqLen;
	}

	// Draw the portion of the grid after the given coordinates at the given scale 
	public void drawGridPart(Graphics g, int size, PreciseNumber x, PreciseNumber y, int scale) {
		Iterator<GridPoint> pitr = points.iterator();
		while(pitr.hasNext()) {
			GridPoint gp = pitr.next();
			gp.drawGrid(g, size*scale/seqLen, x, y);
		}
	}
	
	// find the nearest grid point to the given coordinates
	public GridPoint getNearestGridpoint(double x, double y) {
		GridPoint closest = start;
		GridPoint current = null;
		Iterator<GridPoint> pitr = points.iterator();
		while(pitr.hasNext()) {
			current = pitr.next();
			if(current.distanceTo(x, y) < closest.distanceTo(x, y)) {
				closest = current;
			}
		}
		return closest;
	}
}
