import java.io.PrintStream;

// Exhaustive reducer for computing the number of divisions to get the common refinement
public class NumDivisions implements ExhaustiveSearchReducer<int[]> {

	@Override
	public int[] reduce(int[] first, int[] second) {
		int[] returned = {Math.max(first[0], second[0]),Math.max(first[1], second[1])};
		return returned;
	}

	@Override
	public int[] getValue(Grid grid) {
		return grid.divisionTypes();
	}

	@Override
	public int[] initialValue() {
		int[] returned = {0,0};
		return returned;
	}

	@Override
	public void display(int[] values, int numSteps, int alg, PrintStream ps) {
		ps.println("Algorithm " + alg + "\nNumber of type 1 divisions for " + numSteps + " divisions: " + values[0] +
				"\nNumber of type 3 divisions for " + numSteps + " divisions: " + values[1]);
				
	}

}
