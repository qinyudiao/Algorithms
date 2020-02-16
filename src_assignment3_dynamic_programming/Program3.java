public class Program3 {
	
	/* OPT optimal amount of coins for sequence from start to i
	 * OPT(i, i+1) = 0
	 * OPT(i, i+2) = min(cities[i], cities[i+1])
	 * sum(i, j) = sum of cities between i and j where i is inclusive and j is exclusive
	 * wall(i) = k, where the largest k that satisfies that OPT[i][k] + sum[i][k] > OPT[k+1][j] + sum[k+1][j]
	 * OPT(i, j) = min(OPT[i][w] + sum[i][w], OPT[w][j] + sum[w][j]) where j - 2 > i, and w is wall[i][j]
	 */
	
	int OPT[][] = new int[Driver3.cities.length][Driver3.cities.length+1];
	int sum[][] = new int[Driver3.cities.length][Driver3.cities.length+1];
	int wall[] = new int[Driver3.cities.length-1];

    public int maxCoinValue (int[] cities) {    	
    	int num_cities = cities.length;
    	
    	// OPT(i, i+1) = 0
    	if(num_cities >= 1) {
    		for( int i = 0; (i + 1) <= num_cities; i++ ) {
    			sum[i][i+1] = cities[i];
    			OPT[i][i+1] = 0;
    		}
    	}
    	
    	// OPT(i, i+2) = min(cities[i], cities[i+1])
    	if(num_cities >= 2) {
    		for( int i = 0; (i + 2) <= num_cities; i++ ) {
    			sum[i][i+2] = cities[i] + cities[i+1];
    			OPT[i][i+2] = Math.min(cities[i], cities[i+1]);
    			wall[i] = i+1;
    		}
    	
    	
    	} 	
    	for( int d = 3; d <= num_cities; d++ ) {
	    	for( int i = 0; (i + d) <= num_cities; i++ ) {
	    		sum[i][i+d] = sum[i][i+d-1] + cities[i+d-1];
	    		OPT[i][i+d] = findOPTValue(cities, i, i+d);
	    	}
    	}
    	
        return OPT[0][num_cities];
    }
      
    public int findOPTValue(int[] cities, int start, int end) {
    	
    	int w = wall[start];
    	
    	while(w+1 < end) {
    		if( OPT[w+1][end] + sum[w+1][end] > OPT[start][w] + sum[start][w] ) {
    			w++;
    		}
    		else break;
    	}
    	
    	int max = Math.min(OPT[start][w] + sum[start][w], OPT[w][end] + sum[w][end]);
    	
    	wall[start] = w;
    	
    	return max;
    }
	
	/*
	 * public int maxCoinValue (int[] cities) { // Implement your dynamic
	 * programming algorithm here // You may use helper functions as needed
	 * 
	 * int num_cities = cities.length;
	 * 
	 * if(num_cities >= 1) { for( int i = 0; (i + 1) <= num_cities; i++ ) {
	 * sum[i][i+1] = cities[i]; OPT[i][i+1] = 0; } }
	 * 
	 * if(num_cities >= 2) { for( int i = 0; (i + 2) <= num_cities; i++ ) {
	 * sum[i][i+2] = cities[i] + cities[i+1]; OPT[i][i+2] = Math.min(cities[i],
	 * cities[i+1]); } }
	 * 
	 * for( int d = 3; d <= num_cities; d++ ) { for( int i = 0; (i + d) <=
	 * num_cities; i++ ) { sum[i][i+d] = sum[i][i+d-1] + cities[i+d-1]; OPT[i][i+d]
	 * = findOPTValue(cities, i, i+d); } }
	 * 
	 * return OPT[0][num_cities]; }
	 * 
	 * 
	 * public int findOPTValue(int[] cities, int start, int end) {
	 * 
	 * int max = 0; for( int i = start + 1; i < end; i++ ) { max = Math.max(max,
	 * (Math.min((OPT[start][i] + sum[start][i]), (OPT[i][end] + sum[i][end])))); }
	 * return max; }
	 */
}



