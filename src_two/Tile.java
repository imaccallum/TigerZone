
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
	
	public void rotate() {
		// Pass parameter or 2 separate functions for clockwise v CCW
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
