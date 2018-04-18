
public class LinkedStack<T> implements Stack<T>{
	private Node<T> head;
	private int size;
	
	@SuppressWarnings("hiding")
	public class Node<T> {
		private T value;
		private Node<T> next;
		
		public Node() {
			
		}
		public Node(T value) {
			this.value = value;
		}
		public Node(T value, Node<T> next) {
			this(value);
			this.next = next;
		}
		
		public T getValue() {
			return value;
		}
		
		public Node<T> getNext(){
			return next;
		}
	}
	
	public boolean isEmpty() {
		return head == null;
	}
	
	public void push(T thing) {
		if(head == null) {
			head = new Node<T>(thing);
		} else {
			head = new Node<T>(thing, head);
		}
		size++;
	}
	
	public T peek() {
		if(!isEmpty()) {
			return head.getValue();
		} else {
			throw new UnsupportedOperationException("Cannot pop an empty stack");
		}
	}
	
	public T pop() {
		T temp = peek();
		head = head.getNext();
		size--;
		return temp;
	}
}
