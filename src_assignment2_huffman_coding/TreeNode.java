public class TreeNode {
	private TreeNode leftChild;
	private TreeNode rightChild;
	private TreeNode parent;
	private String value;
	private int frequency;

	TreeNode() {
	    this.leftChild = null;
	    this.rightChild = null;
	    this.parent = null;
	    this.value = null;
	    this.frequency = 0;
	}

	public void setLeftChild(TreeNode child) {
		this.leftChild = child;
	}

	public void setRightChild(TreeNode child) {
		this.rightChild = child;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public void setValue(String value){
	    this.value = value;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public TreeNode getLeftChild(){
	    return this.leftChild;
	}

	public TreeNode getRightChild(){
	    return this.rightChild;
	}

	public TreeNode getParent(){
	    return this.parent;
	}

	public String getValue(){
	    return this.value;
	}

	public int getFrequency(){
	    return this.frequency;
	}
	
	@Override
    public String toString() {
    	if( leftChild == null && rightChild == null )
    		return getFrequency() + "";
    	else if(rightChild == null)
    		return getFrequency() + " has leftChild " + leftChild.toString();
    	else if(leftChild == null)
    		return getFrequency() + " has rightChild " + rightChild.toString();    	
        return getFrequency() + " has leftChild "+ leftChild.toString() + " has rightChild " +rightChild.toString(); 
    }
	
}
