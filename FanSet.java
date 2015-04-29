import java.awt.Graphics;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

// This is an object representation for the entire fan (no matter the original basis)
public class FanSet {
	// The fan contains the set of the points, the set of edges, and the set of triangles in it
	private HashSet<Point> points;
	private HashSet<Edge> edges;
	private HashSet<Triangle> triangles;
	
	// This creates a default fan, using the standard basis for 3D space as the original points,
	// constructing the edges between them as well, and the triangle in the middle
	public FanSet() {
		Point[] p = {new Point(1, 0, 0), new Point(0, 1, 0),
				new Point(0, 0, 1)};
		Edge[] e = {new Edge(p[0], p[1]), new Edge(p[0], p[2]),
				new Edge(p[1], p[2])};
		Triangle t = new Triangle(p[0], p[1], p[2], e[0], e[1], e[2]);
		p[0].addEdge(e[0]);
		p[0].addEdge(e[1]);
		p[1].addEdge(e[0]);
		p[1].addEdge(e[2]);
		p[2].addEdge(e[1]);
		p[2].addEdge(e[2]);
		p[0].addTriangle(t);
		p[1].addTriangle(t);
		p[2].addTriangle(t);
		e[0].addTriangle(t);
		e[1].addTriangle(t);
		e[2].addTriangle(t);
		points = new HashSet<Point>(Arrays.asList(p));
		edges = new HashSet<Edge>(Arrays.asList(e));
		triangles = new HashSet<Triangle>();
		triangles.add(t);
	}
	
	// This constructs a fan that contains the given points, edges, and triangles, which must be
	// consistent with each other, and have the proper relations
	public FanSet(HashSet<Point> points, HashSet<Edge> edges,
			HashSet<Triangle> triangles) {
		this.points = points;
		this.edges = edges;
		this.triangles = triangles;
	}
	
	// This returns a copy of the fan, with all internal references duplicated so it can be
	// modified without damaging the copied fan
	public FanSet deepCopy() {
		// Create new instances of each point
		Point[] oldPoints = points.toArray(new Point[points.size()]);
		Point[] newPoints = new Point[oldPoints.length];
		for(int i = 0; i < oldPoints.length; i++) {
			newPoints[i] = new Point(oldPoints[i].getX(), oldPoints[i].getY(),
					oldPoints[i].getZ());
		}
		
		// Create new instances of each edge
		Edge[] oldEdges = edges.toArray(new Edge[edges.size()]);
		Edge[] newEdges = new Edge[oldEdges.length];
		for(int i = 0; i < oldEdges.length; i++) {
			int p1 = -1;
			int p2 = -1;
			for(int j = 0; j < newPoints.length; j++) {
				if(oldEdges[i].hasPoint(newPoints[j])) {
					if(p1 == -1) {
						p1 = j;
					} else {
						p2 = j;
					}
				}
			}
			newEdges[i] = new Edge(newPoints[p1],
					newPoints[p2]);
			newPoints[p1].addEdge(newEdges[i]);
			newPoints[p2].addEdge(newEdges[i]);
		}
		
		// Creates new instances of each triangle
		Triangle[] oldTriangles = triangles.toArray(new
				Triangle[triangles.size()]);
		Triangle[] newTriangles = new Triangle[oldTriangles.length];
		for(int i = 0; i < oldTriangles.length; i++) {
			int p1 = -1;
			int p2 = -1;
			int p3 = -1;
			for(int j = 0; j < newPoints.length; j++) {
				if(oldTriangles[i].hasPoint(newPoints[j])) {
					if(p1 == -1) {
						p1 = j;
					} else if(p2 == -1) {
						p2 = j;
					} else {
						p3 = j;
					}
				}
			}
			int e1 = -1;
			int e2 = -1;
			int e3 = -1;
			for(int j = 0; j < newEdges.length; j++) {
				if(oldTriangles[i].hasEdge(newEdges[j])) {
					if(e1 == -1) {
						e1 = j;
					} else if(e2 == -1) {
						e2 = j;
					} else {
						e3 = j;
					}
				}
			}
			newTriangles[i] = new Triangle(newPoints[p1],
					newPoints[p2], newPoints[p3], newEdges[e1],
					newEdges[e2], newEdges[e3]);
			newPoints[p1].addTriangle(newTriangles[i]);
			newPoints[p2].addTriangle(newTriangles[i]);
			newPoints[p3].addTriangle(newTriangles[i]);
			newEdges[e1].addTriangle(newTriangles[i]);
			newEdges[e2].addTriangle(newTriangles[i]);
			newEdges[e3].addTriangle(newTriangles[i]);
		}
		
		// Create the new fan
		return new FanSet(
				new HashSet<Point>(Arrays.asList(newPoints)),
				new HashSet<Edge>(Arrays.asList(newEdges)),
				new HashSet<Triangle>(Arrays.asList(newTriangles)));
	}
	
	// Returns the set of points in the fan
	public HashSet<Point> getPoints() {
		return points;
	}
	
	// Returns the set of edges in the fan
	public HashSet<Edge> getEdges() {
		return edges;
	}
	
	// Returns the set of triangles in the fan
	public HashSet<Triangle> getTriangles() {
		return triangles;
	}
	
	// Divides the fan along the given edge if it exists in the fan and returns whether it succeeded
	public boolean divideOnEdge(Edge e) {
		// null edges succeed
		if(e == null) {
			return true;
		}
		// edges not in the fan fail
		if(!edges.contains(e)) {
			return false;
		}
		// this recovers the edge in the fan that matches the given edge
		// this is so that all of the associated information can be used in the division
		Edge[] arr = edges.toArray(new Edge[edges.size()]);
		for(int i = 0; i < arr.length; i++) {
			if(arr[i].equals(e)) {
				e = arr[i];
			}
		}
		// add the midpoint of the edge to the fan
		Point mid = e.midpoint();
		points.add(mid);
		Point[] ends = e.getPoints();
		// remove the edge from the fan
		edges.remove(e);
		// add the two new edges created by dividing the original edge to the fan
		Edge e0 = new Edge(ends[0], mid);
		ends[0].removeEdge(e);
		ends[0].addEdge(e0);
		edges.add(e0);
		Edge e1 = new Edge(ends[1], mid);
		ends[1].removeEdge(e);
		ends[1].addEdge(e1);
		edges.add(e1);
		
		// process each of the triangles that contain the edge
		Triangle[] faces = e.getTriangles();
		for(int i = 0; i < faces.length; i++) {
			// remove the triangle from the fan
			triangles.remove(faces[i]);
			Point[] corners = faces[i].getPoints();
			Edge newEdge = null;
			for(int j = 0; j < 3; j++) {
				// delete internal references to the triangle
				corners[j].removeTriangle(faces[i]);
				if(!e.hasPoint(corners[j])) {
					// calculate and add the new edge created through this triangle
					newEdge = new Edge(mid, corners[j]);
					edges.add(newEdge);
				}
			}
			Edge[] sides = faces[i].getEdges();
			for(int j = 0; j < 3; j++) {
				// delete internal references to the triangle
				sides[j].removeTriangle(faces[i]);
				if(!sides[j].equals(e)) {
					Triangle newTri;
					Point[] p = sides[j].getPoints();
					// construct new triangles created by the new point and add it to the fan
					if(sides[j].hasPoint(ends[0])) {
						newTri = new Triangle(p[0], p[1],
								mid, e0, newEdge, sides[j]);
						e0.addTriangle(newTri);
					} else {
						newTri = new Triangle(p[0], p[1],
								mid, e1, newEdge, sides[j]);
						e1.addTriangle(newTri);
					}
					newEdge.addTriangle(newTri);
					sides[j].addTriangle(newTri);
					p[0].addTriangle(newTri);
					p[1].addTriangle(newTri);
					mid.addTriangle(newTri);
					triangles.add(newTri);
				}
			}
		}
		return true;
	}
	
	// This method returns the edge in the fan that has the given point as its midpoint if one exists
	// This is useful for other classes, as well as for division on a point instead of an edge
	public Edge midToEdge(Point p) {
		Edge e;
		Iterator<Edge> itr = edges.iterator();
		while(itr.hasNext()) {
			e = itr.next();
			if(p.equals(e.midpoint())) {
				return e;
			}
		}
		return null;
	}
	
	// This method returns a printable representation of the fan, which is a list of the points,
	// edges, and triangles in the fan
	public String toString() {
		String str = "Points:";
		Iterator<Point> pitr = points.iterator();
		while(pitr.hasNext()) {
			str += "\n" + pitr.next().toString();
		}
		str += "\nEdges:";
		Iterator<Edge> eitr = edges.iterator();
		while(eitr.hasNext()) {
			str += "\n" + eitr.next().toString();
		}
		str += "\nTriangles:";
		Iterator<Triangle> titr = triangles.iterator();
		while(titr.hasNext()) {
			str += "\n" + titr.next().toString();
		}
		return str;
	}
	
	// This returns the number of points in the fan
	public int numPoints() {
		return points.size();
	}
	
	// This returns the number of edges in the fan
	public int numEdges() {
		return edges.size();
	}
	
	// This returns the number of triangles in the fan
	public int numTriangles() {
		return triangles.size();
	}
	
	// This returns a random sequence of edges of the given length with this fan as the beginning
	public Edge[] randSeq(int len) {
		Edge[] toReturn = new Edge[len];
		FanSet fan = deepCopy();
		for(int i = 0; i < len; i++) {
			HashSet<Edge> e = fan.getEdges();
			toReturn[i] = e.toArray(new Edge[e.size()])[(int)(Math.random()*e.size())];
			fan.divideOnEdge(toReturn[i]);
		}
		return toReturn;
	}
	
	// This draws the fan at the given size
	public void drawFan(Graphics g, int size) {
		Iterator<Edge> itr = edges.iterator();
		while(itr.hasNext()) {
			itr.next().drawEdge(g, size);
		}
	}
	
	// This returns the length of the longest vector (point) in the fan
	public int getLongestLength() {
		int max = 0;
		Iterator<Point> itr = points.iterator();
		while(itr.hasNext()) {
			max = Math.max(max, itr.next().getLength());
		}
		return max;
	}
	
	// This returns a list of the lengths of the generators of the fan
	public int[] getPointsDists() {
		int[] list = new int[getLongestLength()];
		Iterator<Point> pitr = points.iterator();
		while(pitr.hasNext()) {
			int size = pitr.next().getLength();
			list[size-1]++;
		}
		return list;
	}
	
	// This gets a list of the new edges created when the fan is divided along the given edge
	public Edge[] getNewEdges(Edge e){
		Point mid = e.midpoint();
		Triangle[] faces = e.getTriangles();
		Edge[] newEdges = new Edge[faces.length];
		for(int i = 0; i < faces.length; i++) {
			Point[] corners = faces[i].getPoints();
			for(int j = 0; j < 3; j++) {
				if(!e.hasPoint(corners[j])) {
					// calculate and add the new edge created through this triangle
					newEdges[i] = new Edge(mid, corners[j]);
				}
			}
		}
		return newEdges;
	}
	
	
	// This gets the longest length of the vectors in the fan and in the intersection of the given edges with the fan
	public int getLongestIntersectionLength(Edge[] e) {
		int max = 0;
		Iterator<Point> pitr = points.iterator();
		while(pitr.hasNext()) {
			max = Math.max(max, pitr.next().getLength());
		}
		for (int i = 0; i < e.length; i++) {
		Iterator<Edge> eitr = edges.iterator();
			while(eitr.hasNext()) {
				Point p = eitr.next().intersectionPoint(e[i]);
				if (p != null) {
					max = Math.max(max, p.getLength());
				}
			}
		}
		return max;
	}
	
	// Returns the nearest edge to the given coordinates, for use in an interactive fan builder
	public Edge nearestEdge(double x, double y) {
		Edge current = null;
		double dist = -1;
		Iterator<Edge> eitr = edges.iterator();
		while(eitr.hasNext()) {
			Edge e = eitr.next();
			if(e.distanceTo(x, y) != -1 && (dist == -1 || e.distanceTo(x, y) < dist)) {
				current = e;
				dist = e.distanceTo(x, y);
			}
		}
		return current;
	}
}
