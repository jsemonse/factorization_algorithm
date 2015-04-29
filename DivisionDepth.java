import java.io.PrintStream;

// Exhaustive search for the maximum number of divisions that affect each other.
public class DivisionDepth implements ExhaustiveSearchReducer<Integer> {

	@Override
	public Integer reduce(Integer first, Integer second) {
		return Math.max(first, second);
	}

	@Override
	public Integer getValue(Grid grid) {
		return grid.getDeepestGridCoord();
	}

	@Override
	public Integer initialValue() {
		return 0;
	}

	@Override
	public void display(Integer values, int numSteps, int alg, PrintStream ps) {
		ps.println("Algorithm " + alg + "\nDeepest GridPoint coordinates for " + numSteps + " divisions: " + values);
	}

}
