package cheatingAtTheCasino;

import java.util.ArrayList;
import java.util.List;

/**
 * Homework 8, Problem 3: Cheating at the Casino
 * 
 * You are in a casino that offers the following game: n cards are placed in some order, upside down, on the table. 
 * On the side you can’t see, each card has a number written on it, that can be positive or negative.
 * If you choose not to play the game, you don’t win or lose anything. If you play, you can select a start point s, and an endpoint e such that e ≥ s. 
 * Then, the cards from s to e are turned face up. Let T be the sum of the numbers written on them. If T is positive, you win T dollars. 
 * If T is negative, you pay -T dollars. However, you have cheated and so you know what the numbers on each card are. 
 * Provide a linear algorithm that tells you if you should play or not, and, if yes, finds s and e such that T is maximized.
 * e.g. if the numbers on the cards are: 5, 15, -30, 10, -5, 40, 10 
 * then the answer should be s = 4 and e = 7, and you would win 55 dollars.
*/

public class Driver {

	public static void main(String[] args) {
		
		//int[] nums = {5, 15, -30, 10, -5, 40, 10};
		int[] nums = {5, 15, -30, -10, 10, -5, 40, 10, -50, 80, -100, -23, 234, 12};
		List<Integer> numsOnCards = new ArrayList<>();
		
		for(int num : nums) {
			numsOnCards.add(num);
		}
		
		int[] result = getMaxT(numsOnCards, numsOnCards.size()-1);
		
		int maxT = result[1];
		int s = result[3];
		int e = result[4];
		System.out.println("maxT is " + maxT);
		System.out.println("s is " + s);
		System.out.println("e is " + e);
		

	}

	static int[] getMaxT(List<Integer> nums, int index) {
		//result[0] is temporary max, result[1] is all time max, 
		//result [2] is temp starting index, result [3-4] is indexs for all time max
		int[] result = new int[5];
		int[] zeros = {0, 0, 0, 0, 0};
		result =( index <= 0) ? zeros : getMaxT(nums, index-1);

		result[0] = Math.max(0, nums.get(index) + result[0]);
		
		if(result[0] == 0)
			result[2] = index;
		
		else if(result[0] > result[1]) {
			result[3] = result[2] + 2;
			result[4] = index + 1;
			result[1] = result[0];
		}
		
		System.out.println(index + " " + result[0] + " " + result[1] + " " + result[3]);
		return result;
	}
}
