public class DLB {
	private int total_elements;
	private Node head;
	
	public DLB() {
		total_elements = 0;
		head = new Node();
	}

	public void insertWord(String s){
		int ndx = 0;
		Node currNode = head;
		
		while(ndx < s.length()){
		char currChar = s.charAt(ndx);
		
			if(currNode.getChild() == null){
				
				currNode.setChild(new Node(null, null, currChar));
				total_elements++;
				currNode = currNode.getChild();

			} else {
				currNode = currNode.getChild();
				
				while(currNode.getChar() != currChar) {
					
					if(currNode.getNext() == null) {
						currNode.setNext(new Node(null, null, currChar));
					}
					
					currNode = currNode.getNext();	

				}
			}
			ndx++;
		}
		
		currNode.setChild(new Node(null, null, '^'));
	}

	public Node getHead(){
		return this.head;
	}

	public int getNumElements(){
		return this.total_elements;
	}
}