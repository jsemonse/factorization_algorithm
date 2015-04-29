import java.awt.Graphics;
import java.awt.Color;

// This class represents a point in the grid that represents the path of the algorithm
// It contains the fan, the next points in the grid, the edges that represent the changes,
// plus the coordinates in the grid (for use in potential invariants)
public class GridPoint implements Comparable<GridPoint>{
	private final FanSet fan;
	//private float x;
	//private float y;
	private final PreciseNumber x;
	private final PreciseNumber y;
	private Edge xEdge;
	private GridPoint xNext;
	private Edge yEdge;
	private GridPoint yNext;
	
	public static int CIRC_RADIUS = 4;
	
	// This creates a default point on the grid with the default fan, no connections,
	// and with coordinates (0,0)
	public GridPoint() {
		fan = new FanSet();
		//this.x = 0;
		//this.y = 0;
		this.x = new PreciseNumber(0);
		this.y = new PreciseNumber(0);
		xEdge = null;
		xNext = null;
		yEdge = null;
		yNext = null;
	}
	// This creates a point in the grid with the given fan and coordinates, with no connections
	// public GridPoint(FanSet f, float x, float y) {
	public GridPoint(FanSet f, int x, int y) {
		fan = f;
		this.x = new PreciseNumber(x);
		this.y = new PreciseNumber(y);
		xEdge = null;
		xNext = null;
		yEdge = null;
		yNext = null;
	}

	// This creates a point in the grid with the given fan and coordinates, with no connections
	// public GridPoint(FanSet f, float x, float y) {
	public GridPoint(FanSet f, PreciseNumber x, PreciseNumber y) {
		fan = f;
		this.x = x;
		this.y = y;
		xEdge = null;
		xNext = null;
		yEdge = null;
		yNext = null;
	}
	
	// This determines if the point is ready to be used for the algorithm (if it has points in
	// either direction which have not already been shown to be commutative
	public boolean toDivide() {
		if ((xNext == null) || (yNext == null)) {
			return false;
		} else {
			return xNext.getYNext() == null && yNext.getXNext() == null;
		}
	}
	
	// This returns what type of commutative diagram is needed for the given algorithm
	// Type 1 is the original algorithm
	// Type 2 uses the original algorithm except when the other type of type 3 algorithm will be more efficient
	// Type 3 uses the other type of type 3 algorithm except when the original algorithm is needed (otherwise endless loop
	// Type 4 uses the original algorithm except when the newly created edge appears before the
	// edge it is factored with in the following sequence
	// Return values:
	// 0 means that the algorithm doesn't work yet (nothing)
	// 1 means that the edges each way are the same (triangle)
	// 2 means that the edges don't share a triangle, so they commute (square)
	// 3 means that the edges do share a triangle, so we use the standard algorithm
	// 4 means that it is a type 3 where we want to use the modified algorithm
	public int divisionType(int alg) {
		if(!toDivide()) {
			return 0;
		}
		if(xEdge == null || yEdge == null) {
			return 2;
		}
		if (xEdge.equals(yEdge)) {
			return 1;
		}
		Triangle[] t = fan.midToEdge(xEdge.midpoint()).getTriangles();
		for(int i = 0; i < t.length; i++) {
			if(fan.midToEdge(yEdge.midpoint()).hasTriangle(t[i])) {
				if(xEdge.equals(yNext.getYEdge())) {
					return 3;
				}
				Point[] p = t[i].getPoints();
				Edge newEdge = null;
				for(int j = 0; j < 3; j++) {
					if(!yEdge.hasPoint(p[j])){
						newEdge = new Edge(yEdge.midpoint(), p[j]);
					}
				}
				if(alg > 1) {
					if(newEdge.equals(yNext.getYEdge())) {
						return 4;
					}
				}
				if(alg == 4) {
					GridPoint current = yNext.getYNext();
					while(current != null) {
						if(xEdge.equals(current.getYEdge())) {
							return 3;
						} else if(newEdge.equals(current.getYEdge())) {
							return 4;
						}
						current = current.getYNext();
					}
				}
				if(alg == 3) {
					return 4;
				}
				return 3;
			}
		}
		return 2;
	}
	
	/*public boolean setXEdge(Edge e) {
		HashSet<Edge> edges = fan.getEdges();
		if(edges.contains(e)) {
			Iterator<Edge> itr = edges.iterator();
			while(itr.hasNext() && !xEdge.equals(e)) {
				xEdge = itr.next();
			}
			return true;
		}
		return false;
	}
	
	public boolean setYEdge(Edge e) {
		HashSet<Edge> edges = fan.getEdges();
		if(edges.contains(e)) {
			Iterator<Edge> itr = edges.iterator();
			while(itr.hasNext() && !yEdge.equals(e)) {
				yEdge = itr.next();
			}
			return true;
		}
		return false;
	}*/
	
	// This sets the edge that is divided on to get to the next point in the x direction
	public boolean setXEdge(Edge e) {
		xEdge = e;
		return true;
	}
	
	// This sets the edge that is divided on to get to the next point in the y direction
	public boolean setYEdge(Edge e) {
		yEdge = e;
		return true;
	}
	
	// This sets the next point in the x direction
	public boolean setXNext(GridPoint gp) {
		xNext = gp;
		return true;
	}
	
	// This sets the next point in the y direction
	public boolean setYNext(GridPoint gp) {
		yNext = gp;
		return true;
	}
	
	// This returns the x coordinate
	// public float getX() {
	public PreciseNumber getX() {
		return x;
	}
	
	// This returns the y coordinate
	// public float getY() {
	public PreciseNumber getY() {
		return y;
	}
	
	// This returns the edge that is divided on to get to the next point in the x direction
	public Edge getXEdge() {
		return xEdge;
	}
	
	// This returns the edge that is divided on to get to the next point in the y direction
	public Edge getYEdge() {
		return yEdge;
	}
	
	// This returns the next point in the x direction
	public GridPoint getXNext() {
		return xNext;
	}
	
	// This returns the next point in the y direction
	public GridPoint getYNext() {
		return yNext;
	}
	
	// This returns the fan associated with this point
	public FanSet getFan() {
		return fan;
	}
	
	// This returns a printable representation of the point: the coordinates, then the associated fan
	public String toString() {
		//return "Coords (" + x + ", "  + y + ")\n" + fan.toString();
		return "Coords (" + x.toString() + ", "  + y.toString() + ")\n" + fan.toString();
	}
	
	// This returns the number of points in the fan at this grid point
	public int numPoints() {
		return fan.numPoints();
	}
	
	// This returns the number of edges in the fan at this grid point
	public int numEdges() {
		return fan.numEdges();
	}
	
	// This returns the number of triangles in the fan at this grid point
	public int numTriangles() {
		return fan.numTriangles();
	}
	
	// this draws the fan of the specified size
	public void drawFan(Graphics g, int size) {
		fan.drawFan(g, size);
	}
	
	// Get the coordinates for drawing this grid point
	public double[] getDrawCoords(PreciseNumber x, PreciseNumber y) {
		double[] r = {PreciseNumber.subtract(this.x, x).approxValue(), PreciseNumber.subtract(this.y, y).approxValue()};
		return r;
	}
	
	//draw this grid point in the given graphics pane at the given size with the given start point
	public void drawGrid(Graphics g, int size, PreciseNumber x, PreciseNumber y) {
		//System.out.println("Drawing grid point (" + x.approxValue() + ", " + y.approxValue() + ")");
		double[] drawCoords = getDrawCoords(x, y);
		double[] xCoord = null;
		double[] yCoord = null;
		if(xNext != null) {
			xCoord = xNext.getDrawCoords(x, y);
		}
		if(yNext != null) {
			yCoord = yNext.getDrawCoords(x, y);
		}
		
		if(xNext != null && yNext != null) {
			if(xEdge != null && xEdge.equals(yEdge)) {
				g.drawLine((int)(size*xCoord[1]), (int)(size*xCoord[0]), (int)(size*yCoord[1]), (int)(size*yCoord[0]));
				g.setColor(Color.green);
			} else if(xNext.yNext.compareTo(yNext.xNext) != 0) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.blue);
			}
			g.fillRect((int)(size*drawCoords[1]), (int)(size*drawCoords[0]), (int)(size*(yCoord[1]-xCoord[1]))+1, (int)(size*(xCoord[0]-yCoord[0]))+1);
		}
		
		g.setColor(Color.black);
		g.fillOval((int)(size*drawCoords[1])-(CIRC_RADIUS/2), (int)(size*drawCoords[0])-(CIRC_RADIUS/2), CIRC_RADIUS, CIRC_RADIUS);
		if(xNext != null) {
			g.drawLine((int)(size*drawCoords[1]), (int)(size*drawCoords[0]), (int)(size*xCoord[1]), (int)(size*xCoord[0]));
		}
		if(yNext != null) {
			g.drawLine((int)(size*drawCoords[1]), (int)(size*drawCoords[0]), (int)(size*yCoord[1]), (int)(size*yCoord[0]));
		}
	}
	
	// Gives a comparator between points in the grid
	@Override
	public int compareTo(GridPoint g) {
		if(x.equals(g.getX())) {
			return y.compareTo(g.getY());
		}
		return x.compareTo(g.getX());
	}
	
	// Compute Euclidean distance between these grid points
	public double distanceTo(double testX, double testY) {
		double deltaX = testX - x.approxValue();
		double deltaY = testY - y.approxValue();
		return Math.sqrt((deltaX*deltaX) + (deltaY*deltaY));
	}
}
