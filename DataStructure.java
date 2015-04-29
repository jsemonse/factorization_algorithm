// This class is a simple structure so that PointsDistance2 can store all the needed data
public class DataStructure {
		// The various data types saved by the exhaustive reducer
		public int furthestDistance;
		public FanSet furthestDistanceFan;
		public Edge[] furthestDistanceInitial;
		public FanSet furthestDistanceInitialFan;
		
		public int furthestOldDistance;
		public FanSet furthestOldDistanceFan;
		public Edge[] furthestOldDistanceInitial;
		public FanSet furthestOldDistanceInitialFan;
		
		public int largestDifference;
		public FanSet largestDifferenceFan;
		public Edge[] largestDifferenceInitial;
		public FanSet largestDifferenceInitialFan;
		
		public DataStructure() {
			furthestDistance = 0;
			furthestDistanceFan = null;
			furthestDistanceInitial = null;
			furthestDistanceInitialFan = null;
			furthestOldDistance = 0;
			furthestOldDistanceFan = null;
			furthestOldDistanceInitial = null;
			furthestOldDistanceInitialFan = null;
			largestDifference = 0;
			largestDifferenceFan = null;
			largestDifferenceInitial = null;
			largestDifferenceInitialFan = null;
		}
		
		public DataStructure(Grid g) {
			furthestDistance = g.farthestLength();
			furthestDistanceFan = g.lastFan();
			furthestDistanceInitial = g.getInitialSeq();
			furthestDistanceInitialFan = g.initialFan();
			furthestOldDistance = g.farthestOldLength();
			furthestOldDistanceFan = furthestDistanceFan;
			furthestOldDistanceInitial = furthestDistanceInitial;
			furthestOldDistanceInitialFan = furthestDistanceInitialFan;
			largestDifference = furthestDistance - furthestOldDistance;
			largestDifferenceFan = furthestDistanceFan;
			largestDifferenceInitial = furthestDistanceInitial;
			largestDifferenceInitialFan = furthestDistanceInitialFan;
		}
	}