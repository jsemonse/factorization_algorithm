import java.awt.Graphics;
import java.util.HashSet;

// This is the object that represents an edge, or the division between two fans.
// It has two points that fully define the edge, as well as a set of all the
// triangles that contain the edge
public class Edge {
	// These are the points that fully define the edge
	private final Point p1;
	private final Point p2;
	// This is the set of all triangles that contain the edge. This set checks for containment,
	// but must be managed so that it contains all of the triangles
	private HashSet<Triangle> triangles;
	
	// This constructs an edge with no associated triangles, given the end points
	public Edge(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
		triangles = new HashSet<Triangle>();
	}
	
	// This checks if the given point is an endpoint of the edge
	public boolean hasPoint(Point p) {
		return p.equals(p1) || p.equals(p2);
	}
	
	// This adds the given triangle to the set if it contains the edge
	public void addTriangle(Triangle triangle) {
		if (triangle.hasEdge(this)) {
			triangles.add(triangle);
		}
	}
	
	// This removes the given triangle from the set
	public void removeTriangle(Triangle triangle) {
		triangles.remove(triangle);
	}
	
	// This overrides the equals method so that edges are equal if they have the 
	// same endpoints (regardless of order and associated triangles)
	@Override
	public boolean equals(Object o) {
		if(o == null || o.getClass() != this.getClass()) {
			return false;
		}
		return ((Edge)o).hasPoint(p1) && ((Edge)o).hasPoint(p2);
	}
	
	// This overrides the hashCode method so that equal edges have the same hash code
	// so that the HashSet structure works correctly
	public int hashCode(){
		return p1.hashCode()^p2.hashCode();
	}
	
	// This calculates the midpoint of the edge
	public Point midpoint() {
		return new Point(p1.getX() + p2.getX(), p1.getY() + p2.getY(), p1.getZ() + p2.getZ());
	}
	
	// This returns the endpoints of the edge
	public Point[] getPoints() {
		Point[] p = {p1, p2};
		return p;
	}
	
	// This returns the triangles the edge is part of
	public Triangle[] getTriangles() {
		return triangles.toArray(new Triangle[triangles.size()]);
	}
	
	// This checks if the edge contains the given triangle
	public boolean hasTriangle(Triangle t) {
		return triangles.contains(t);
	}
	
	// This returns a textual representation of the edge, described by its endpoints
	public String toString() {
		return "[" + p1.toString() + "," + p2.toString() + "]";
	}
	
	// This draws the edge at the size specified
	public void drawEdge(Graphics g, int size) {
		double[] c1 = p1.getDrawCoords();
		double[] c2 = p2.getDrawCoords();
		g.drawLine((int)(c1[0]*size), (int)(c1[1]*size), (int)(c2[0]*size), (int)(c2[1]*size));
	}
	
	// This returns the intersection point between this edge and the given edge if one exists, and null if not
	public Point intersectionPoint(Edge e) {
		if(e == null || equals(e)) {
			return null;
		}
		Point q1 = e.getPoints()[0];
		Point q2 = e.getPoints()[1];
		Point v1 = p1.crossProduct(p2);
		Point v2 = q1.crossProduct(q2);
		if (p1.dotProduct(v2)*p2.dotProduct(v2) > 0 || q1.dotProduct(v1)*q2.dotProduct(v1) > 0) {
			return null;
		}
		Point p = v1.crossProduct(v2);
		if(p.getX() < 0 || p.getY() < 0 || p.getZ() < 0) {
			return p.negative();
		}
		return p;
	}
	
	// Compute the distance to this edge
	public double distanceTo(double x, double y) {
		double[] c1 = p1.getDrawCoords();
		double[] c2 = p2.getDrawCoords();
		double dx = c2[0] - c1[0];
		double dy = c2[1] - c1[1];
		double length2 = (dx*dx) + (dy*dy);
		double px = x - c1[0];
		double py = y - c1[1];
		double dotProd = px*dx + py*dy;
		double multiple = dotProd/length2;
		if(0 <= multiple && 1 >= multiple){
			double difx = px - (multiple*dx);
			double dify = py - (multiple*dy);
			double diflen = (difx*difx) + (dify*dify);
			return Math.sqrt(diflen);
		} else {
			return -1;
		}
	}
}
