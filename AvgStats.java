import java.io.PrintStream;

// Computes the average (instead of the maximum) number of cones in the fans.
public class AvgStats implements ExhaustiveSearchReducer<int[]> {
	@Override
	public int[] reduce(int[] first, int[] second) {
		int[] returned = {first[0] + second[0], first[1] + second[1],
				first[2] + second[2], first[3] + second[3]};
		return returned;
	}

	@Override
	public int[] getValue(Grid grid) {
		int[] returned = {grid.numPoints(), grid.numEdges(), grid.numTriangles(), 1};
		return returned;
	}

	@Override
	public int[] initialValue() {
		int[] returned = {0, 0, 0, 0};
		return returned;
	}

	@Override
	public void display(int[] values, int numSteps, int alg, PrintStream ps) {
		ps.println("Algorithm " + alg + "\nAverage points for " + numSteps + " divisions: " + (values[0]/values[3]) +
				"\nAverage edges for " + numSteps + " divisions: " + (values[1]/values[3]) +
				"\nAverage triangles for " + numSteps + " divisions: " + (values[2]/values[3]));
	}
}
