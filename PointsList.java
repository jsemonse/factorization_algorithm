import java.io.PrintStream;

// Data structure for tracking (maximizing) the lengths of the generators
public class PointsList implements ExhaustiveSearchReducer<int[]> {

	@Override
	public int[] reduce(int[] first, int[] second) {
		if(first.length < second.length) {
			return second;
		}
		if(second.length < first.length) {
			return first;
		}
		int i = first.length- 1;
		while(i >= 0) {
			if(first[i] < second[i]){
				return second;
			}
			if(second[i] < first[i]){
				return first;
			}
			i--;
		}
		return first;
	}

	@Override
	public int[] getValue(Grid grid) {
		return grid.getPointsDists();
	}

	@Override
	public int[] initialValue() {
		return new int[1];
	}

	@Override
	public void display(int[] values, int numSteps, int alg, PrintStream ps) {
		ps.println("For algorithm " + alg + " the maximum distribution of distances for " + numSteps + " steps is:");
		for(int i = 0; i < values.length; i++) {
			ps.println((i + 1) + ": " + values[i]);
		}
	}
}
