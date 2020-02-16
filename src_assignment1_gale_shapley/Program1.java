//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    private int getPreferenceIndex(ArrayList<Integer> preferenceList, int entity) {
        int index = -1;
        for (int i = 0; i < preferenceList.size(); i++) {
            if (preferenceList.get(i) == entity) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * Compute the preference lists for each internship, given weights and student metrics.
     * Return a ArrayList<ArrayList<Integer>> prefs, where prefs.get(i) is the ordered list of preferred students for
     * internship i, with length studentCount.
     */
    public static ArrayList<ArrayList<Integer>> computeInternshipPreferences(int internshipCount, int studentCount,
                                                                      ArrayList<ArrayList<Integer>>internship_weights,
                                                                      ArrayList<Double> student_GPA,
                                                                      ArrayList<Integer> student_months,
                                                                      ArrayList<Integer> student_projects){
    	
		ArrayList<ArrayList<Integer>> internshipPref = new ArrayList<>(internshipCount);

		for(int i=0; i<internshipCount; i++) {
			double [] temp_score = new double [studentCount];
			int [] temp_rank = new int [studentCount];
			
			for(int j=0; j<studentCount; j++) {
				temp_rank[j] = j;
				temp_score[j] = computeInternshipStudentScore(student_GPA.get(j), student_months.get(j), student_projects.get(j), 
				  internship_weights.get(i).get(0), internship_weights.get(i).get(1),
				  internship_weights.get(i).get(2));
				//System.out.print(temp_rank[j] + " : " + temp_score[j] + "  |  "); //print our scores and corresponding ranks
			}
			
			//System.out.print("Company " + i + " prefers ");
			temp_rank = _get_sorted(studentCount, temp_rank, temp_score);
			
			internshipPref.add(new ArrayList<Integer>());
			for(int j=0; j<studentCount; j++) {	
				internshipPref.get(i).add(temp_rank[j]);
			}
		}
		
		return internshipPref;
    }
    private static Double computeInternshipStudentScore(double studentGPA, int studentExp, int studentProjects, int
                                                        weightGPA, int weightExp, int weightProjects){
        return studentGPA*weightGPA+studentExp*weightExp+studentProjects*weightProjects;
    }

    /**
     * Determines whether a candidate Matching represents a solution to the Stable Marriage problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
        ArrayList<ArrayList<Integer>> student_pref = marriage.getStudentPreference();
        ArrayList<ArrayList<Integer>> internship_pref = marriage.getInternshipPreference();
        ArrayList<Integer> student_matching = marriage.getStudentMatching();
//        System.out.println(Arrays.toString(student_matching.toArray()));
        
        /* First type of instability: There are students s and s', and an internship I, such that
		 *  - s is assigned to I, and
		 *	- s' is assigned to no internship, and
		 *	- I prefers s' to s. */      
        for(int i=0; i<student_matching.size(); i++){ //if s' is unassigned (-1), get id of s', check internship prefs
        	if(student_matching.get(i) == -1){
        		for(int j=0; j<internship_pref.size(); j++) { //check internship prefs
        			//id of s' = i
        			int internship_j_preference_index_of_student_i = getPreferenceIndex(internship_pref.get(j), i); // index of s' on preference list of internship j 
        			
        			/*********NEEDS TO CHECK THE SITUATION OF ONE INTERNSHIP HAS MULTIPLE SLOTS***********/
        			int max_slots = marriage.getInternshipSlots().get(j);
        			int index_of_slot = 0;
        			while(index_of_slot < max_slots) {
	        			int id_of_s = _getNextMatchedStudentIndex(student_matching, j, index_of_slot); //id of s who is assigned to internship j
	        			/* student is unassigned, internship needs student */
	        			if (id_of_s == -1) return false; //if internship j does not have an assigned student s, then unstable matching
	        			
	        			int index_of_s = getPreferenceIndex(internship_pref.get(j), id_of_s);
	        			if(internship_j_preference_index_of_student_i < index_of_s) return false; //if I prefers s' to s, then unstable matching
	        			
	        			index_of_slot ++;
        			}
        		}
        	}
    	}
        
        /*  Second type of instability: There are students s and s', and internships I and I', so that
		 *	- s is assigned to I, and
		 *	- s'is assigned to I', and
		 *	- I prefers s' to s, and 
		 *	- s' prefers I to I'. */
        for(int i=0; i<student_matching.size(); i++){ //
        	int id_of_internship_prime = student_matching.get(i); //id of internship that student i / s' is assigned to
        	//id of s' = i
        	int student_i_preference_index_of_internship_prime = getPreferenceIndex(student_pref.get(i), id_of_internship_prime);

			
    		for(int j=0; j<internship_pref.size(); j++) { //check internship prefs, j is id of internship
    			
    			//*********NEEDS TO CHECK THE SITUATION OF ONE INTERNSHIP HAS MULTIPLE SLOTS***********//
    			int max_slots = marriage.getInternshipSlots().get(j);
    			int index_of_slot = 0;
    			
    			while(index_of_slot < max_slots) {
	    			int internship_j_preference_index_of_student_i = getPreferenceIndex(internship_pref.get(j), i);
					int student_i_preference_index_of_internship_j = getPreferenceIndex(student_pref.get(i), j);
	    			//id of I is j
	    			if(id_of_internship_prime != j) {
	    				int id_of_s = _getNextMatchedStudentIndex(student_matching, j, index_of_slot);
	    				if(id_of_s != i) {
		    				int internship_j_preference_index_of_s = getPreferenceIndex(internship_pref.get(j), id_of_s);		    				
		    				boolean I_prefers_s_to_s_prime = internship_j_preference_index_of_student_i < internship_j_preference_index_of_s;
		    				boolean s_prime_prefers_I_to_I_prime = student_i_preference_index_of_internship_j < student_i_preference_index_of_internship_prime;
		    				
	//	    				System.out.print("\ns_" + id_of_s);
	//	    				System.out.println(" I_" + j);
	//	    				System.out.print("s'_" + i);
	//	    	        	System.out.println(" I'_" + id_of_internship_prime);
	//	        			System.out.print("I(" + j +")_pref__s' " + internship_j_preference_index_of_student_i);
	//	    				System.out.print(" s " + internship_j_preference_index_of_s);
	//    					System.out.println("      " + I_prefers_s_to_s_prime);
	//	        			System.out.print("s'(" + i + ")_pref_I " + student_i_preference_index_of_internship_j);
	//	        			System.out.print(" I' " + student_i_preference_index_of_internship_prime);
	//	        			System.out.println("      " + s_prime_prefers_I_to_I_prime + "\n");
		        			
		    				if(I_prefers_s_to_s_prime && s_prime_prefers_I_to_I_prime) {
	
		    					return false; //if I prefers s' to s and s' prefers I to I'
		    				}
	    				}
	    			}
	    			index_of_slot++;
    			} //end of while loop
    			

    		}      	
    	}  
        return true;
    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_studentoptimal(Matching marriage) {
    	Matching GS_studentoptimal = new Matching(marriage);
    	
    	ArrayList<Integer> student_matching = new ArrayList<Integer>();
    	
//    	System.out.println("InternshipPreference: " + GS_studentoptimal.getInternshipPreference());
//    	System.out.println("StudentPreference: " + GS_studentoptimal.getStudentPreference());
//    	System.out.println("InternshipSlots: " + GS_studentoptimal.getInternshipSlots());
//    	System.out.println("TotalSlots: " + GS_studentoptimal.totalInternshipSlots());
    	
    	ArrayList<Integer> internship_slots = GS_studentoptimal.getInternshipSlots();
    	@SuppressWarnings("unchecked")
		ArrayList<Integer> max_slots = (ArrayList<Integer>) internship_slots.clone();
    	ArrayList<ArrayList<Integer>> student_pref_list = GS_studentoptimal.getStudentPreference(); //this list will be updated
    	ArrayList<ArrayList<Integer>> internship_pref_list = GS_studentoptimal.getInternshipPreference();
    	
    	/* Matching student optimal, therefore, student requests an internship, and internship accepts*/
    	for(int i=0; i<GS_studentoptimal.getStudentCount(); i++) { //initialize the matching with -1, meaning empty at the start
    		student_matching.add(-1);
    	}
    	
    	int count = 0;
    	while(count!=-1) { 
    		for(int i=0; i<GS_studentoptimal.getStudentCount(); i++) { //i is the index of Student in student_matching
    			
    			if((count==0 || student_matching.get(i) == -1)) { //only attempt to changes the value on first iteration or == -1 (not matched)
	    			int student_i_highest_rank = student_pref_list.get(i).get(count);
	    			//if internship slots is not full in the current match, accepts the student
	    			if(internship_slots.get(student_i_highest_rank) > 0) {
	    				student_matching.set(i, student_i_highest_rank); //student_matching updates
	    				internship_slots.set(student_i_highest_rank, internship_slots.get(student_i_highest_rank)-1); //company available slots -1
	    			}
	    			else {
		    			int index_of_slot = 0;
		    			
		    			while(index_of_slot < max_slots.get(student_i_highest_rank)) {
		    				
			    			int id_of_s = _getNextMatchedStudentIndex(student_matching, student_i_highest_rank, index_of_slot);
			    			int highest_rank_index_pref_of_student_i = getPreferenceIndex(internship_pref_list.get(student_i_highest_rank), i); //
			    			int highest_rank_index_pref_of_s = getPreferenceIndex(internship_pref_list.get(student_i_highest_rank), id_of_s); //s is the current assigned student
			    			//if internship prefers s' to one student in the current match, accepts the student, drop the previous one
			    			if(highest_rank_index_pref_of_student_i < highest_rank_index_pref_of_s) {
			    				student_matching.set(i, student_i_highest_rank); //student_matching updates
			    				student_matching.set(id_of_s, -1); //the previous student is unassigned
			    			}
			    			index_of_slot ++;
		    			} //end of inner while loop
	    			}
    			}
    		}
    		//System.out.println(student_matching);
    		count ++;
    		if(count == GS_studentoptimal.getInternshipCount()) break;
    		/* if every student has an internship, matching is complete and stable (only in the case without shortage of internships) */
    		int num = 0; // num_of_student_has_an_internship 
    		for(int assigned_internship : student_matching) {
    			if(assigned_internship == -1) 
    				break;
    			else
    				num++;
    			if(num == GS_studentoptimal.getStudentCount())
    				count = -1;
    		}
    	} //end of outer while loop
    	
    	GS_studentoptimal.setStudentMatching(student_matching);
        return GS_studentoptimal; 
    }

    private ArrayList<Matching> getAllStableMarriages(Matching marriage) {
        ArrayList<Matching> marriages = new ArrayList<>();
        int n = marriage.getStudentCount();
        int slots = marriage.totalInternshipSlots();

        Permutation p = new Permutation(n, slots);
        Matching matching;
        while ((matching = p.getNextMatching(marriage)) != null) {
            if (isStableMatching(matching)) {
                marriages.add(matching);
            }
        }

        return marriages;
    }

    @Override
    public Matching stableMarriageBruteForce_studentoptimal(Matching marriage) {
        ArrayList<Matching> allStableMarriages = getAllStableMarriages(marriage);
        Matching studentOptimal = null;
        int n = marriage.getStudentCount();
        int m = marriage.getInternshipCount();
        System.out.println("student" + n + "internship" + m);
        ArrayList<ArrayList<Integer>> student_preference = marriage.getStudentPreference();

        //Construct an inverse list for constant access time
        ArrayList<ArrayList<Integer>> inverse_student_preference = new ArrayList<ArrayList<Integer>>(0) ;
        for (Integer i=0; i<n; i++) {
            ArrayList<Integer> inverse_preference_list = new ArrayList<Integer>(m) ;
            for (Integer j=0; j<m; j++)
                inverse_preference_list.add(-1) ;
            ArrayList<Integer> preference_list = student_preference.get(i) ;

            for (int j=0; j<m; j++) {
                inverse_preference_list.set(preference_list.get(j), j) ;
            }
            inverse_student_preference.add(inverse_preference_list) ;
        }

        // bestStudentMatching stores the rank of the best Internship each student matched to
        // over all stable matchings
        int[] bestStudentMatching = new int[marriage.getStudentCount()];
        Arrays.fill(bestStudentMatching, -1);
        for (Matching mar : allStableMarriages) {
            ArrayList<Integer> student_matching = mar.getStudentMatching();
            for (int i = 0; i < student_matching.size(); i++) {
                if (student_matching.get(i) != -1 && (bestStudentMatching[i] == -1 ||
                        inverse_student_preference.get(i).get(student_matching.get(i)) < bestStudentMatching[i])) {
                    bestStudentMatching[i] = inverse_student_preference.get(i).get(student_matching.get(i));
                    studentOptimal = mar;
                }
            }
        }

        return studentOptimal;
    }
    
    private static int[] _get_sorted(int studentCount, int[]temp_rank, double[] temp_score) {
    	int temp_studentCount = studentCount;
    	for(int i=0; i<studentCount; i++) {
			for(int j=1; j<temp_studentCount; j++) {
				if (temp_score[i] < temp_score[i+j]) {
					double temp0 = temp_score[i+j];
					temp_score[i+j] = temp_score[i];
					temp_score[i] = temp0;
					
					int temp1 = temp_rank[i+j];
					temp_rank[i+j] = temp_rank[i];
					temp_rank[i] = temp1;
				}
			}
			temp_studentCount --;
		}
    	System.out.println(Arrays.toString(temp_rank));
		return temp_rank;
    }
    
    private int _getNextMatchedStudentIndex(ArrayList<Integer> student_matching, int internship, int index_of_slot) {
        int index = -1;
        int count = 0;
        for (int i = 0; i < student_matching.size(); i++) {
            if (student_matching.get(i) == internship) {
                index = i;
                if(count == index_of_slot)
                	break;
                count ++;  
            }
        }
        return index;
    }
}
