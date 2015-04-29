
// This is an object that represents a triangle of 3 eges and 3 points (which in 3 space
// is an entire fan, but is not in higher dimensions).
public class Triangle {
	// These are the points that determine the triangle
	private final Point p1;
	private final Point p2;
	private final Point p3;
	// These are the edges between those points (these are not checked for validity)
	private final Edge e1;
	private final Edge e2;
	private final Edge e3;
	
	// This constructs a triangle with the given points and edges
	public Triangle(Point p1, Point p2, Point p3,
			Edge e1, Edge e2, Edge e3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
	}
	
	// This method checks if the given point is in the triangle
	public boolean hasPoint(Point p) {
		return p.equals(p1) || p.equals(p2) || p.equals(p3);
	}
	
	// This method checks if the given edge is in the triangle
	public boolean hasEdge(Edge e) {
		return e.equals(e1) || e.equals(e2) || e.equals(e3);
	}
	
	// This method overrides the equals method so that two triangles are equal if their points are
	// equal (in no particular order). Edges and descriptors are not considered.
	@Override
	public boolean equals(Object o) {
		if(o == null || o.getClass() != this.getClass()) {
			return false;
		}
		return ((Triangle)o).hasPoint(p1) &&
				((Triangle)o).hasPoint(p2) &&
				((Triangle)o).hasPoint(p3);
	}
	
	// This overrides the hashCode method so equal triangles have the same hash code
	@Override
	public int hashCode(){
		return p1.hashCode() ^ p2.hashCode() ^ p3.hashCode();
	}
	
	// This method returns an array of the points in the triangle so other classes can use the data
	public Point[] getPoints() {
		Point[] p = {p1, p2, p3};
		return p;
	}
	
	// This method returns an array of the edges in the triangle so other classes can use the data
	public Edge[] getEdges() {
		Edge[] e = {e1, e2, e3};
		return e;
	}
	
	// This method returns a printable representation of the triangle, represented by the 3 points
	public String toString() {
		return "{" + p1.toString() + "," + p2.toString() +
				"," + p3.toString() + "}";
	}
}
