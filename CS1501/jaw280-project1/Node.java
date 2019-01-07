public class Node{
	private Node nextNode;
	private Node childNode;
	private char character;
	private String slice;

	public Node(){
		nextNode = null;
		childNode = null;
		character = '%';
		slice = "";
	}

	public Node(Node next, Node child, char c){
		nextNode = next;
		childNode = child;
		character = c;
		slice = "";
	}

	public Node getNext(){
		return this.nextNode;
	}

	public Node getChild(){
		return this.childNode;
	}

	public char getChar(){
		return this.character;
	}

	public String getSlice(){
		return this.slice;
	}

	public void setNext(Node next){
		this.nextNode = next;
	}

	public void setChild(Node child){
		this.childNode = child;
	}

	public void setChar(char c){
		this.character = c;
	}

	public void setSlice(String s){
		this.slice = s;
	}

	public boolean hasNext(){
		if(this.nextNode != null){
			return true;
		}
		return false;
	}

	public boolean hasChild(){
		if(this.childNode != null){
			return true;
		}
		return false;
	}
}