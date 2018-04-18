public class ArrayQueue<T> implements Queue<T> {
	private int size = 0;
	private int read = 0;
	private int write = 0;
	private Object values[] = new Object[10];
	
	private void ensureCapacity() {
		if(size > 0 && read == write) {
			Object temp[] = new Object[values.length*2 + 1];
			System.arraycopy(values, read, temp, 0, values.length-read);
			System.arraycopy(values, 0, temp, values.length-read, read-1);
			values = temp;
			read = 0;
			write = size;
		}
	};
	
	public T remove() {
		T result = peek();
		read++;
		if(read >= values.length) {
			read = 0;
		}
		size--;
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public T peek() {
		if(isEmpty()) throw new UnsupportedOperationException("Can't peek at an empty queue");
		return (T) values[read];
	}
	
	public void add(T thing) {
		ensureCapacity();
		values[write] = thing;
		write++;
		if(write >= values.length) {
			write = 0;
		}
		size++;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
}
