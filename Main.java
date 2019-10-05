import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Main {

	public static void main(String[] args) {
		int n = 4;
		int[][] costs = {{0,1,1}, {0,2,2}, {1,2,5}, {1,3,1}, {2,3,8}}; // return 4
		
		System.out.println(new Solution().solution(n, costs));
	}
}

class Solution {
	class Edge {
		int start, dest, weight;

		public Edge(int start, int dest, int weight) {
			this.start = start;
			this.dest = dest;
			this.weight = weight;
		}

		public String toString() {
			return "(" + start + ", " + dest + ") : " + weight;
		}
	}
	
	Comparator<Edge> compByWeight = new Comparator<Edge>() {
		public int compare(Edge e1, Edge e2) {
			return e1.weight - e2.weight;
		}
	};
	
    public int solution(int n, int[][] costs) {
    	int totalCost = 0;
    	int[] subSet = new int[n];
    	for (int i = 0; i < subSet.length; i++) {
    		subSet[i] = i;
    	}
    	
    	HashSet<Integer> usedNodes = new HashSet<Integer>();
        PriorityQueue<Edge> heap = new PriorityQueue<>(compByWeight);
        
        for (int[] cost : costs) {
        	Edge edge = new Edge(cost[0], cost[1], cost[2]);
        	heap.add(edge);
        }
        
        
        while (!checkSet(subSet)) {
        	Edge edge = heap.poll();

        	if (!usedNodes.contains(edge.start) || !usedNodes.contains(edge.dest) || !(subSet[edge.start] == subSet[edge.dest])) {
        		usedNodes.add(edge.start);
        		usedNodes.add(edge.dest);
        		totalCost += edge.weight;
        		
        		if (subSet[edge.start] != subSet[edge.dest]) {
        			int parentNum = subSet[edge.start];
        			int childNum = subSet[edge.dest];
        			for (int i = 0; i < subSet.length; i++) {
        				if (subSet[i] == childNum)
        					subSet[i] = parentNum;
        			}
        		}
        	}
        }
        
        return totalCost;
    }
    
	private boolean checkSet(int[] subSet) {
		int setNum = subSet[0];
		for (int i = 0; i < subSet.length; i++) {
			if (subSet[i] != setNum)
				return false;
		}
		return true;
	}
}