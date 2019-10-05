# Programmers.Level3_Java_IslandConnection

# 프로그래머스 > 탐욕법(Greedy) > 섬 연결하기

### 1. 문제설명

문제: https://programmers.co.kr/learn/courses/30/lessons/42861

input으로 섬의 개수 `n`과 섬과 섬사이의 통행 비용을 담은 `int[][] costs`가 들어온다. `costs`를 구성하는 각 배열은 `시작섬, 도착섬, 비용`으로 구성된다. 최소의 비용으로 모든 섬이 서로 통행 가능하도록 만들 때 필요한 최소 비용을 return하라.

* 제한사항
> 연결할 수 없는 섬은 주어지지 않으며, 모든 섬 사이의 다리 건설 비용이 주어지지 않는다.

### 2. 풀이

최소신장트리(MST)를 만드는 문제와 동일하여 크루스칼 알고리즘을 이용하여 접근하였다. `costs`로 받아올 정보를 담을 `Edge`클래스를 만들어 비용에 대하여 오름차순으로 정렬하기위해 `Comparator`를 선언하고 저장에 우선순위큐인 `PriorityQueue`를 사용하였다. 

```java
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
  ...
  PriorityQueue<Edge> heap = new PriorityQueue<>(compByWeight);

  for (int[] cost : costs) {
    Edge edge = new Edge(cost[0], cost[1], cost[2]);
    heap.add(edge);
  }
  ...
}
```

가장 낮은 비용을 가진 Edge부터 추가하며, 추가된 Node들은 HashSet에 기록하여 사용여부를 체크한다. 이때 문제점으로 예를 들어 `n=4`, Edge `(0,1,1)`와 `(2,3,2)`가 추가된다면 모든 Node가 사용되었다. 하지만 `0`,`1`번 섬이 연결되었고 `2`,`3`섬이 연결되었을 뿐, 서로 다른 집합에 속하게된다. 이를 위해 속해있는 집합을 저장하는 배열을 별도로 두어 같은 집합에 추가되었을 때 속한 집합의 번호를 저장하도록 하였다.

```java
int totalCost = 0;
int[] subSet = new int[n];
// 자신이 속한 집합 초기화: i번 섬은 집합번호 i에 속한다.
for (int i = 0; i < subSet.length; i++) {
  subSet[i] = i;
}

while (!checkSet(subSet)) {
  Edge edge = heap.poll();

  if (!usedNodes.contains(edge.start) || !usedNodes.contains(edge.dest) || !(subSet[edge.start] == subSet[edge.dest])) {
    usedNodes.add(edge.start);
    usedNodes.add(edge.dest);
    totalCost += edge.weight;

// 같은 집합에 속하지 않았을 때 dest의 집합에 속한 모든 섬들을 start의 집합에 속하도록 수정한다.
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
```

while문 안의 `checkSet`함수는 모든 집합의 번호가 동일할 때 `true`를 return하여 모든 섬이 연결되었는지 확인하는 역할을 한다.

```java
private boolean checkSet(int[] subSet) {
  int setNum = subSet[0];
  for (int i = 0; i < subSet.length; i++) {
    if (subSet[i] != setNum)
      return false;
  }
  return true;
}
```
