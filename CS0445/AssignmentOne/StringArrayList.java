public class StringArrayList implements StringList{
	
	private String list[] = new String[0];
	private int nextItem = 0;
	
	private void ensureCapacity() {
		if(nextItem == list.length) {
			String temp[] = new String[list.length*2 + 1];
			System.arraycopy(list, 0, temp, 0, list.length);
			list = temp;
		}
	}
	
	public int add(String s) {
		ensureCapacity();
		list[nextItem] = s;
		nextItem++;
		return nextItem-1;
	}
	
	public String get(int i) {
		return list[i];
	}
	
	public boolean contains(String s) {
		for(int i = 0; i<nextItem; i++) {
			if(list[i].equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public int indexOf(String s) {
		for(int i = 0; i<nextItem; i++) {
			if(s.equals(list[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public int size() {
		return nextItem;
	}
	
	public int add(int index, String s) {
		ensureCapacity();
		for(int i = nextItem; i>index; i--) {
			list[i] = list[i-1];
		}
		list[index] = s;
		nextItem++;
		return index;
	}
	
	
	public void clear() {
		nextItem = 0;
	}
	
	public boolean isEmpty() {
		if(nextItem == 0) {
			return true;
		}
		return false;
	}
	
	public String remove(int i) {
		ensureCapacity();
		if(this.isEmpty()) {
			throw new IndexOutOfBoundsException("Array is empty.");
		}
		String removed = list[i];
		for(int j = i; j<nextItem; j++) {
			list[j] = list[j+1];
		}
		nextItem--;
		return removed;
	}
	
	public void set(int index, String s) {
		list[index]= s;
	}
	
	public String[] toArray() {
		String copy[] = new String[nextItem];
		System.arraycopy(list, 0, copy, 0, nextItem);
		return copy;
	}
}
