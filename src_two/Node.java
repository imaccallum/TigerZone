
public abstract class Node {

	String type;	 // "JUNGLE" || "LAKE" || "TRAIL" || "DEN"
	
	boolean isChain;
	ChainAttribute attributes;	// Store attributes in a shared object so you don't have to iterate through chain to update each nodes values
	
	Node link_A;
	Node link_B;
	
	public Node(String type) {
		this.type = type;
	}
	
	
}
