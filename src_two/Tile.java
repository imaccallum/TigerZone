
public class Tile {
				
	//				Edge[1]
	//		Corner[4]	Corner[1]
	//	Edge[4]					Edge[2]
	//		Corner[3]	Corner[2]
	//				Edge[3]
	
	Node[] edges;					
	Node[] corners;					
	Node center;					
	
	public Tile() {
		edges = new Node[4];
		corners = new Node[4];
	}
	
	public void rotate() {	//will rotate clockwise 90 degrees, call multiple times if desired
		Node tempedge = edges[3];
       		Node tempcorner = corners[3];
        	System.arraycopy(edges, 0, edges, 1, 3);    //shifts all down one
        	System.arraycopy(corners, 0, corners, 1, 3);
		edges[0] = tempedge;
		corners[0] = tempcorner;
	}
	
	public void attachLeft(Tile t) {
		// link corresponding nodes
	}
	
	public void attachRight(Tile t) {
		// link corresponding nodes
	}
	
	public void attachBelow(Tile t) {
		// link corresponding nodes
	}
	public void attachAbove(Tile t) {
		// link corresponding nodes
	}
	

}
