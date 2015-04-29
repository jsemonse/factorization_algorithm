import java.util.Comparator;

// This class is just used as the method of sorting the points in the grid so they print in
// a useful order. The criteria is to sort by X-coordinate and then Y-coordinate
public class GridComparator implements Comparator<GridPoint> {
	// This returns a number used to create a total ordering of the points in the grid
	// This means earlier things have smaller numbers when compared, and only equal things return 0
	// In addition, the operation returns the additive inverse if the arguments are swapped
	public int compare(GridPoint g1, GridPoint g2) {
		if(g1.getX().equals(g2.getX())) {
			return g1.getY().compareTo(g2.getY());
		}
		return g1.getX().compareTo(g2.getX());
	}
}
