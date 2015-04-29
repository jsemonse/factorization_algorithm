import java.io.PrintStream;

// Reducer for getting the longest generators for fans (used in exhaustive search)
public class PointsDistance implements ExhaustiveSearchReducer<int[]> {
	@Override
	public int[] reduce(int[] first, int[] second) {
		int[] returned = {Math.max(first[0], second[0]), Math.max(first[1], second[1]),
				Math.max(first[2], second[2])};
		return returned;
	}

	@Override
	public int[] getValue(Grid grid) {
		int[] returned = {grid.farthestLength(), grid.farthestOldLength(), 0};
		returned[2] = returned[0] - returned[1];
		return returned;
	}

	@Override
	public int[] initialValue() {
		int[] returned = {0, 0, 0};
		return returned;
	}

	@Override
	public void display(int[] values, int numSteps, int alg, PrintStream ps) {
		ps.println("Algorithm " + alg + "\nFarthest distance for " + numSteps + " divisions: " + values[0] +
				"\nFarthest old distance for " + numSteps + " divisions: " + values[1] +
				"\nLargest difference for " + numSteps + " divisions: " + values[2]);
	}
}
