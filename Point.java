import java.util.HashSet;

// This is the object that represents a point in three space that
// is a basis vector for a fan
public class Point {
	// The integer coordinates (in 3-space) of this point
	private final int x;
	private final int y;
	private final int z;
	// This is the set of all the edges that contain the point
	// The set checks that the edges added contain the point, but the
	// other parts of the program must ensure that all are in the set
	private HashSet<Edge> edges;
	// This is the same as above, only for triangles
	private HashSet<Triangle> triangles;
	
	
	// This method creates a point with the specified coordinates
	// with no associated edges or triangles
	public Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		edges = new HashSet<Edge>();
		triangles = new HashSet<Triangle>();
	}
	
	// This method adds an edge to the set if the edge contains the point
	public void addEdge(Edge edge) {
		if (edge.hasPoint(this)) {
			edges.add(edge);
		}
	}
	
	// This method removes the specified edge from the set
	public void removeEdge(Edge edge) {
		edges.remove(edge);
	}
	
	// This method adds an triangle to the set if the triangle contains the point
	public void addTriangle(Triangle triangle) {
		if (triangle.hasPoint(this)) {
			triangles.add(triangle);
		}
	}
	
	// This method removes the triangle from the set
	public void removeTriangle(Triangle triangle) {
		triangles.remove(triangle);
	}
	
	// This method overrides the equals comparator so that a point is
	// equal to all others with the same coordinates (and not edges or
	// triangles or memory address)
	@Override
	public boolean equals(Object o){
		if(o == null || o.getClass() != this.getClass()) {
			return false;
		}
		return x == ((Point)o).getX() && y == ((Point)o).getY() && z == ((Point)o).getZ();
	}
	
	// This method does the same thing as the equals method, only creates
	// a hash code that is the same for equal objects. This ensures that
	// the HashSet structure works with the above criteria for equality
	@Override
	public int hashCode(){
		return 31 * 31 * x + 31 * y + z;
	}
	
	// This returns an array of the edges stored by this point so other classes can access it
	public Edge[] getEdges() {
		return edges.toArray(new Edge[edges.size()]);
	}
	
	// This returns an array of the triangles stored by this point so other classes can access it
	public Triangle[] getTriangles() {
		return triangles.toArray(new Triangle[triangles.size()]);
	}
	
	// This returns a printable representation of this point (by coordinates)
	public String toString() {
		return "(" + x +"," + y + "," + z + ")";
	}
	
	// This returns the x-coordinate of this point
	public int getX() {
		return x;
	}
	
	// This returns the y-coordinate of this point
	public int getY() {
		return y;
	}
	
	// This returns the z-coordinate of this point
	public int getZ() {
		return z;
	}
	
	// This returns the coordinates of this point for 2d projection when using the standard basis
	// (the original triangle is projected to the plane) 
	public double[] getDrawCoords() {
		double scale = 1 / Math.sqrt(3);
		double length = getLength();
		double[] returned = new double[2];
		returned[0] = (((double)z/length) - ((double)x/length) + 1)*scale;
		returned[1] = 1 - ((double)y / length);
		return returned;
	}
	
	// This returns the length of the vector (in square coordinates)
	// This is also the number of vectors that were added together to make the vector represented
	public int getLength() {
		return x + y + z;
	}
	
	// This returns the negative of a point (useful in intersection calculations)
	public Point negative() {
		return new Point(-x, -y, -z);
	}
	
	// This returns the cross product of this point with the given point, scaled to the smallest possible integer coordinates 
	public Point crossProduct(Point p) {
		if(equals(new Point(0, 0, 0)) || p.equals(new Point(0, 0, 0)) || equals(p) || equals(p.negative())) {
			return new Point(0, 0, 0);
		}
		int newX = (y*p.getZ()) - (z*p.getY());
		int newY = (z*p.getX()) - (x*p.getZ());
		int newZ = (x*p.getY()) - (y*p.getX());
		int gcd = gcd(newX, gcd(newY, newZ));
		return new Point(newX/gcd, newY/gcd, newZ/gcd);
	}
	
	// This is used to calculate the GCD of two numbers for the cross product scaling
	private int gcd(int a, int b) {
		if(Math.abs(a) > Math.abs(b)) {
			return gcdHelper(Math.abs(a), Math.abs(b));
		}
		return gcdHelper(Math.abs(b), Math.abs(a));
	}
	
	// This is the recursive GCD calculator that uses the Euclidean algorithm
	private int gcdHelper(int a, int b) {
		return b==0 ? a : gcdHelper(b, a%b);
	}
	
	// This returns the dot product of this point with the given point
	public int dotProduct(Point p) {
		return (x*p.getX()) + (y*p.getY()) + (z*p.getZ());
	}
}
