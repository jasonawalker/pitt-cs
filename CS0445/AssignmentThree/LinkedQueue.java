public class LinkedQueue<T> implements Queue<T> {
	private Node<T> head;
	private Node<T> tail;
	private int size = 0;
	
	@SuppressWarnings("hiding")
	private class Node<T> {
		private T value;
		private Node<T> next;
		
		public Node(T value) {
			this.value = value;
		}
		
		public T getValue() {
			return value;
		}
		
		public Node<T> getNext() {
			return next;
		}
		
		public void setNext(Node<T> next) {
			this.next = next;
		}
	}
	
	public void add(T thing) {
		if(head == null) {
			head = new Node<T>(thing);
			tail = head;
		} else {
			Node<T> temp = new Node<T>(thing);
			tail.setNext(temp);
			tail = temp;
		}
		size++;
	}
	
	public T peek() {
		if(isEmpty()) throw new UnsupportedOperationException("Can't peek at an empty queue");
		return head.getValue();
	}
	
	public T remove() {
		T result = peek();
		head = head.getNext();
		size--;
		return result; 
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
}
