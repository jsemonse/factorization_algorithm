// This class is a simple structure so that AllStats can store all the needed data
public class DataStructure2 {
	
		// These are the various statistics captured by the exhaustive reducer 
		public final int furthestDistance;
		
		public final int furthestOldDistance;
		
		public final int largestDifference;
		
		public final int type3divs;
		
		public final int type1divs;
		
		public final int maxDepth;
		
		public final int maxPoints;
		
		public final int maxEdges;
		
		public final int maxTris;
		
		public final int[] pointsList;
		
		public DataStructure2() {
			furthestDistance = 0;
			furthestOldDistance = 0;
			largestDifference = 0;
			type3divs = 0;
			type1divs = 0;
			maxDepth = 0;
			maxPoints = 0;
			maxEdges = 0;
			maxTris = 0;
			pointsList = null;
		}
		
		public DataStructure2(Grid g) {
			furthestDistance = g.farthestLength();
			furthestOldDistance = g.farthestOldLength();
			largestDifference = furthestDistance - furthestOldDistance;
			type3divs = g.divisionTypes()[1];
			type1divs = g.divisionTypes()[0];
			maxDepth = g.getDeepestGridCoord();
			maxPoints = g.numPoints();
			maxEdges = g.numEdges();
			maxTris = g.numTriangles();
			pointsList = g.getPointsDists();
		}
		
		private DataStructure2(int fd, int fod, int ld, int t3d, int t1d, int md, int mp, int me, int mt, int[] pl) {
			furthestDistance = fd;
			furthestOldDistance = fod;
			largestDifference = ld;
			type3divs = t3d;
			type1divs = t1d;
			maxDepth = md;
			maxPoints = mp;
			maxEdges = me;
			maxTris = mt;
			pointsList = pl;
		}
		
		public static DataStructure2 combine(DataStructure2 a, DataStructure2 b) {
			int[] pl = null;
			if(a.pointsList == null) {
				pl = b.pointsList;
			} else if(b.pointsList == null) {
				pl = a.pointsList;
			} else if(a.pointsList.length < b.pointsList.length) {
				pl = b.pointsList;
			} else if(b.pointsList.length < a.pointsList.length) {
				pl = a.pointsList;
			} else {
				int i = a.pointsList.length- 1;
				while(i >= 0) {
					if(a.pointsList[i] < b.pointsList[i]){
						pl = b.pointsList;
						break;
					}
					if(b.pointsList[i] < a.pointsList[i]){
						pl = a.pointsList;
						break;
					}
					i--;
				}
				if(pl == null) {
					pl = a.pointsList;
				}
			}
			return new DataStructure2(Math.max(a.furthestDistance, b.furthestDistance),
					Math.max(a.furthestOldDistance, b.furthestOldDistance),
					Math.max(a.largestDifference, b.largestDifference),
					Math.max(a.type3divs, b.type3divs),
					Math.max(a.type1divs, b.type1divs),
					Math.max(a.maxDepth, b.maxDepth),
					Math.max(a.maxPoints, b.maxPoints),
					Math.max(a.maxEdges, b.maxEdges),
					Math.max(a.maxTris, b.maxTris),
					pl);
		}
	}