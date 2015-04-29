import java.io.PrintStream;

// A statistics class for use in exhaustive search. Depends heavily on the data structure DataStructure2.
public class AllStats implements ExhaustiveSearchReducer<DataStructure2> {

	@Override
	public DataStructure2 reduce(DataStructure2 first, DataStructure2 second) {
		return DataStructure2.combine(first, second);
	}

	@Override
	public DataStructure2 getValue(Grid grid) {
		return new DataStructure2(grid);
	}

	@Override
	public DataStructure2 initialValue() {
		return new DataStructure2();
	}

	@Override
	public void display(DataStructure2 values, int numSteps, int alg, PrintStream ps) {
		ps.println("Algorithm " + alg + " for " + numSteps + " divisions:");
		ps.println("Farthest distance: " + values.furthestDistance +
			"\nFarthest old distance: " + values.furthestOldDistance +
			"\nLargest difference: " + values.largestDifference);
		ps.println("Number of type 3 divisions: " + values.type3divs +
				"\nNumber of type 1 divisions: " + values.type1divs);
		ps.println("Deepest GridPoint coordinates: " + values.maxDepth);
		ps.println("Most points: " + values.maxPoints +
				"\nMost edges: " + values.maxEdges +
				"\nMost triangles: " + values.maxPoints);
		ps.println("Distribution of distances:");
		for(int i = 0; i < values.pointsList.length; i++) {
			ps.println((i + 1) + ": " + values.pointsList[i]);
		}
	}
}
