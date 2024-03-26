package org.example;

public class TravelingSalesmanDynamic {

    private final int[][] distances;
    private final static int n = 16;
    private final static int MAX = 1000000;
    private final static int[][] memo = new int[n + 1][1 << (n + 1)];

    public TravelingSalesmanDynamic(int[][] arr) {
        this.distances = new int[arr.length + 1][ arr.length + 1];

        for(int i = 0; i < arr.length; i++)
            for(int j = 0; j < arr.length; j++)
                this.distances[i+1][j+1] = arr[i][j];
    }

    private int fun(int i, int mask) {
        if (mask == ((1 << i) | 3))
            return distances[1][i];

        if (memo[i][mask] != 0)
            return memo[i][mask];

        int res = MAX;

        for (int j = 1; j <= n; j++)
            if ((mask & (1 << j)) != 0 && j != i && j != 1)
                res = Math.min(res, fun(j, mask & (~(1 << i))) + distances[j][i]);

        return memo[i][mask] = res;
    }

    public int solve() {
        int ans = MAX;
        for (int i = 1; i <= n; i++)
            ans = Math.min(ans, fun(i, (1 << (n + 1)) - 1) + this.distances[i][1]);

        return ans;
    }
}
