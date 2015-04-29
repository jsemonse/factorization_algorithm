import java.io.PrintStream;

// Exhaustive reducer for computing the number of various dimensions of cones in a fan
public class MostStats implements ExhaustiveSearchReducer<int[]> {
	@Override
	public int[] reduce(int[] first, int[] second) {
		int[] returned = {Math.max(first[0], second[0]), Math.max(first[1], second[1]),
				Math.max(first[2], second[2])};
		return returned;
	}

	@Override
	public int[] getValue(Grid grid) {
		int[] returned = {grid.numPoints(), grid.numEdges(), grid.numTriangles()};
		return returned;
	}

	@Override
	public int[] initialValue() {
		int[] returned = {0, 0, 0};
		return returned;
	}

	@Override
	public void display(int[] values, int numSteps, int alg, PrintStream ps) {
		ps.println("Algorithm " + alg + "\nMost points for " + numSteps + " divisions: " + values[0] +
				"\nMost edges for " + numSteps + " divisions: " + values[1] +
				"\nMost triangles for " + numSteps + " divisions: " + values[2]);
	}
}