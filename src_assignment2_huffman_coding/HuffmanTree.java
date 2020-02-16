import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HuffmanTree {
	TreeNode root;
	
	HuffmanTree() {
		root = null;
	}

	public void constructHuffmanTree(ArrayList<String> characters, ArrayList<Integer> freq) {
		if( characters.size() != freq.size() || characters.size() == 0) {
			return;
		}
		//assign each character and freq to a treenode
		ArrayList<TreeNode> treeNodes = new ArrayList<>();
		for( int i = 0; i < freq.size(); i++ ){
			TreeNode leaf = new TreeNode();
			leaf.setValue(characters.get(i));
			leaf.setFrequency(freq.get(i));
			treeNodes.add(leaf);
		}
		
		buildMinHeap(treeNodes);
		//printHeapOfTreeNodes(treeNodes, null);
	
		//build the huffman tree from treeNodes heap
		while(treeNodes.size()>1) {
			TreeNode min1 = extractMin(treeNodes); //extract two minmums from the heap
			TreeNode min2 = extractMin(treeNodes);
			TreeNode internalNode = new TreeNode(); //make an internal node as parent of both, frequency=sum of two minmums
			internalNode.setLeftChild(min1);
			internalNode.setRightChild(min2);
			internalNode.setValue(min1.getValue() + "" + min2.getValue());
			internalNode.setFrequency(min1.getFrequency() + min2.getFrequency());
			min1.setParent(internalNode);
			min1.setParent(internalNode);
			insert(treeNodes, internalNode);
		}
		
		//printHeapOfTreeNodes(treeNodes, "Finished building tree." + treeNodes.size());
		this.root = treeNodes.get(0);
	}
	
	public String encode(String humanMessage) {
		//convert the humanMessage string to a char array for iteration
		char[] messageArr = humanMessage.toCharArray();
		//store code_words in a hashMap
		Map<Character, String> codeWords = this._getCodeWords(this.root, null, new HashMap<Character, String>());
		//System.out.println(codeWords);

		//long startTime = System.nanoTime();
		StringBuilder encodedMessage = new StringBuilder();
		//endcode the message by concatenating codes
		for (char ch : messageArr) {
			if(codeWords.containsKey(ch))
				encodedMessage = encodedMessage.append(codeWords.get(ch));
			else 
				return "invalid message";
		}
		//long endTime = System.nanoTime();
		//System.out.println("Execution time in nanoseconds  : " + (endTime - startTime));
	    return encodedMessage.toString();
	}
	
	public String decode(String encodedMessage) {
		//convert the encodedMessage string to a char array for iteration
		char[] messageArr = encodedMessage.toCharArray();
		//store code_words in a hashMap
		Map<String, Character> codeWords = _reverseMapKeyValue( this._getCodeWords(this.root, null, new HashMap<Character, String>()) );
		//System.out.println(codeWords);
		
		//long startTime = System.nanoTime();
		StringBuilder humanMessage = new StringBuilder();
		StringBuilder code = new StringBuilder();
		//decode the message by concatenating characters
		for (Character ch : messageArr) {			
			code = code.append(ch.toString());
			if(codeWords.containsKey(code.toString())) {
				humanMessage = humanMessage.append(codeWords.get(code.toString()));
				code = new StringBuilder();
			}
		}
		//long endTime = System.nanoTime();
		//System.out.println("Execution time in nanoseconds  : " + (endTime - startTime));
		
		if( !code.toString().equals("") ) //if code is not empty, there's some unrecognized pattern in codeWords
			return "invalid message";
        return humanMessage.toString();
	}
	
	// heapifyUp for min heap
    static <T extends TreeNode> void heapifyMin(ArrayList<T> arr, int n, int i) { 
        int smallest = i; //initialize smallest as root 
        int l = 2 * i + 1; //left = 2i+1 
        int r = 2 * i + 2; //right = 2i+2 
        //if left child is smaller than root 
        if (l < n && arr.get(l).getFrequency() < arr.get(smallest).getFrequency()) 
            smallest = l; 
        //if right child is smaller than smallest so far 
        if (r < n && arr.get(r).getFrequency() < arr.get(smallest).getFrequency()) 
            smallest = r; 
        //if smallest is not root 
        if (smallest != i) { 
            T swap = arr.get(i); 
            arr.set(i, arr.get(smallest));
            arr.set(smallest, swap); 
            heapifyMin(arr, n, smallest); 
        } 
    }  
    // build a Min-Heap from arrayList
    static <T extends TreeNode> void buildMinHeap(ArrayList<T> arr) { 
        //index of last non-leaf node 
        int startIdx = (arr.size() / 2) - 1; 
  
        for (int i = 0; i <= startIdx; i++) { 
            heapifyMin(arr, arr.size(), i); 
        } 
    } 
    // extract min from min heap
    static <T extends TreeNode> T extractMin(ArrayList<T> minHeap) {
    	T min = minHeap.get(0);
    	T swap = minHeap.get(minHeap.size()-1);
    	minHeap.set(0, swap);
    	minHeap.remove(minHeap.size()-1);
    	minHeap.trimToSize();
    	
        heapifyMin(minHeap, minHeap.size(), 0);   	
    	return min;
    }
    // insert an element
    static <T extends TreeNode> void insert(ArrayList<T> minHeap, T element) {
    	minHeap.add(element);
        heapifyMin(minHeap, minHeap.size(), minHeap.size()-1); 
    }
    
    // print Heap 
//    static void printHeapOfTreeNodes(ArrayList<TreeNode> arr, String message){ 
//    	System.out.println();
//    	if(message != null)
//    		System.out.println(message);
//        System.out.println("TreeNodes Heap is:"); 
//        int index = 0;
//        for (TreeNode t : arr) {
//            System.out.print(t.getValue() + ":" + t.getFrequency() + " "); 
//            index ++;
//            if(_isPowerOf2(index+1))
//            	System.out.println();
//        }
//        System.out.println("\n"); 
//    } 
    
//    private static boolean _isPowerOf2(int n) {
//    	if(n>1){
//            while(n%2 == 0)
//                n/=2;
//            if(n == 1)
//                return true;
//        }
//        return false;
//    }
    
    private Map<Character, String> _getCodeWords(TreeNode rootNode, String code, Map<Character, String> codeWords) {
    	if (rootNode == null) {
    		return codeWords; 
    	}
    	if(rootNode.getValue().length() == 1) { //if value length is 1, it's a character node
    		if(code == null) //if there's only one character, it's value length is 1 and code is null
    			code = "0";	//to solve it, assgin code to 0
    		codeWords.put(rootNode.getValue().charAt(0), code);
    		return codeWords; //a character node does not have a child, therefore return rightaway;
    	}
    	if(code == null) {
    		_getCodeWords(rootNode.getLeftChild(), "0", codeWords); 
    		_getCodeWords(rootNode.getRightChild(), "1", codeWords);
    	}else {
    		_getCodeWords(rootNode.getLeftChild(), code + "0", codeWords); 
    		_getCodeWords(rootNode.getRightChild(), code + "1", codeWords);
    	}
    	return codeWords;
    }

    private Map<String, Character> _reverseMapKeyValue(Map<Character, String> codeWords){
    	Map<String, Character> reverseCodeWords = new HashMap<String, Character>();
    	
    	for(Map.Entry<Character, String> entry : codeWords.entrySet()){
    		reverseCodeWords.put(entry.getValue(), entry.getKey());
    	}	
    	return reverseCodeWords;
    }

}
