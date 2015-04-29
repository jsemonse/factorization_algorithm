import java.io.PrintStream;

// Statistics for computing the different kinds of divisions
public class TotalGridPoints implements ExhaustiveSearchReducer<Integer> {
	@Override
	public Integer reduce(Integer first, Integer second) {
		if (first.compareTo(second) < 0) {
			return second;
		}
		return first;
	}

	@Override
	public Integer getValue(Grid grid) {
		return new Integer(grid.numPoints());
	}

	@Override
	public Integer initialValue() {
		return 0;
	}

	@Override
	public void display(Integer values, int numSteps, int alg, PrintStream ps) {
		ps.println("Algorithm " + alg + "\nMost GridPoints for " + numSteps + " divisions: " + values);
	}
}
