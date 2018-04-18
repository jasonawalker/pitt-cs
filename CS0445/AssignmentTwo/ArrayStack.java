
public class ArrayStack<T> implements Stack<T> {
	private int next = 0;
	private Object values[] = new Object[0]; 
	
	private void ensureCapacity() {
		if(values.length == next) {
			Object temp[] = new Object[values.length*2 + 1];
			System.arraycopy(values, 0, temp, 0, values.length);
			values = temp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public T peek() {
		if(!isEmpty()) {
			return (T)values[next-1];
		} else {
			throw new UnsupportedOperationException("Cannot pop an empty stack");
		}
	}
	
	public T pop() {
		T temp = peek();
		next--;
		return temp;
	}
	
	public boolean isEmpty() {
		return next==0;
	}
	
	public void push(T thing) {
		ensureCapacity();
		values[next] = thing;
		next++;
	}
}
