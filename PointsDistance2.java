import java.io.PrintStream;

// This is an upgraded version of the PointsDistance reducer that also stores the aossicated fans with the numbers
public class PointsDistance2 implements ExhaustiveSearchReducer<DataStructure> {
	@Override
	public DataStructure reduce(DataStructure first, DataStructure second) {
		DataStructure returned = new DataStructure();
		
		if(first.furthestDistance < second.furthestDistance) {
			returned.furthestDistance = second.furthestDistance;
			returned.furthestDistanceFan = second.furthestDistanceFan;
			returned.furthestDistanceInitial = second.furthestDistanceInitial;
			returned.furthestDistanceInitialFan = second.furthestDistanceInitialFan;
		} else {
			returned.furthestDistance = first.furthestDistance;
			returned.furthestDistanceFan = first.furthestDistanceFan;
			returned.furthestDistanceInitial = first.furthestDistanceInitial;
			returned.furthestDistanceInitialFan = first.furthestDistanceInitialFan;
		}
		
		if(first.furthestOldDistance < second.furthestOldDistance) {
			returned.furthestOldDistance = second.furthestOldDistance;
			returned.furthestOldDistanceFan = second.furthestOldDistanceFan;
			returned.furthestOldDistanceInitial = second.furthestOldDistanceInitial;
			returned.furthestOldDistanceInitialFan = second.furthestOldDistanceInitialFan;
		} else {
			returned.furthestOldDistance = first.furthestOldDistance;
			returned.furthestOldDistanceFan = first.furthestOldDistanceFan;
			returned.furthestOldDistanceInitial = first.furthestOldDistanceInitial;
			returned.furthestOldDistanceInitialFan = first.furthestOldDistanceInitialFan;
		}
		
		if(first.largestDifference < second.largestDifference) {
			returned.largestDifference = second.largestDifference;
			returned.largestDifferenceFan = second.largestDifferenceFan;
			returned.largestDifferenceInitial = second.largestDifferenceInitial;
			returned.largestDifferenceInitialFan = second.largestDifferenceInitialFan;
		} else {
			returned.largestDifference = first.largestDifference;
			returned.largestDifferenceFan = first.largestDifferenceFan;
			returned.largestDifferenceInitial = first.largestDifferenceInitial;
			returned.largestDifferenceInitialFan = first.largestDifferenceInitialFan;
		}
		
		return returned;
	}

	@Override
	public DataStructure getValue(Grid grid) {
		return new DataStructure(grid);
	}

	@Override
	public DataStructure initialValue() {
		return new DataStructure();
	}

	@Override
	public void display(DataStructure values, int numSteps, int alg, PrintStream ps) {
		Main.drawFan(values.furthestDistanceFan, "furthestDistanceFanAlg"+ alg + "Steps" + numSteps);
		Main.drawFan(values.furthestDistanceInitialFan, "furthestDistanceInitialFanAlg"+ alg + "Steps" + numSteps);
		Main.drawFan(values.furthestOldDistanceFan, "furthestOldDistanceFanAlg"+ alg + "Steps" + numSteps);
		Main.drawFan(values.furthestOldDistanceInitialFan, "furthestOldDistanceInitialFanAlg"+ alg + "Steps" + numSteps);
		Main.drawFan(values.largestDifferenceFan, "largestDifferenceFanAlg"+ alg + "Steps" + numSteps);
		Main.drawFan(values.largestDifferenceInitialFan, "largestDifferenceInitialFanAlg"+ alg + "Steps" + numSteps);
		ps.println("Algorithm " + alg + "\nFarthest distance for " + numSteps + " divisions: " + values.furthestDistance +
				"\nFarthest old distance for " + numSteps + " divisions: " + values.furthestOldDistance +
				"\nLargest difference for " + numSteps + " divisions: " + values.largestDifference);
	}
}
