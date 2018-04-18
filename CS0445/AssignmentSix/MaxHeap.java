public class MaxHeap {

  private static final int DEFAULT_CAPACITY = 20;
  private int nextPosition = 0;
  private long[] values;
  
  public MaxHeap() {
    values = new long[DEFAULT_CAPACITY];
  }
  
  public MaxHeap(int initialCapacity) {
    values = new long[initialCapacity];
  }
  
  public MaxHeap(long[] array) {
    this.values = array;
    for (int i = 1; i < values.length; i++) {
      reheapUp(i);
    }
    nextPosition = values.length;
  }
  
  public static void printArr(long[] arr) {
	  for(int i=0; i < arr.length; i++) {
		  System.out.print(" " + arr[i] + " ");
	  }
	  System.out.println();
  }
  
  public static void main(String[] args) {
	  long[] arr = {25,20,19,12,10,18,14,23,56,78,34,90,11,5,3,8,1,0,12,27,38,39};
	  printArr(arr);
	  heapsort(arr);
	  printArr(arr);
  }
  
  public static void heapsort(long[] array) {
    MaxHeap mHeap = new MaxHeap(array);
	  while(mHeap.nextPosition > 0) {
    		mHeap.remove();
    }
  }
  
  public boolean isEmpty() {
    return nextPosition == 0;
  }
  
  public void add(long value) {
    ensureCapacity();
    values[nextPosition] = value;
    reheapUp(nextPosition);
    nextPosition++;
  }
  
  public long getMax() {
    if (this.isEmpty()) {
      throw new UnsupportedOperationException("Heap is empty.");
    }
    return values[0];
  }
  
  public long remove() {
	  long removed = values[--nextPosition];
	  swap(0, nextPosition);
	  reheapDown(0);
	  return removed;
  }
  
  private void reheapDown(int i) {
    int maxChild = getMaxChildIndex(i);
    if (maxChild > -1) {
      if (values[i] < values[maxChild]) {
        swap(i, maxChild);
        reheapDown(maxChild);
      }
    }
  }
  
  private void reheapUp(int i) {
    int parent = (i - 1) / 2;
    if (parent >= 0) {
      if (values[parent] < values[i]) {
        swap(i, parent);
        reheapUp(parent);
      }
    }
  }
    
  private void ensureCapacity() {
    if (nextPosition >= values.length) {
      long[] temp = new long[values.length * 2 + 1];
      System.arraycopy(values, 0, temp, 0, values.length);
      values = temp;
    }
  }
  
  private void swap(int a, int b) {
    long temp = values[a];
    values[a] = values[b];
    values[b] = temp;
  }

  private int getMaxChildIndex(int i) {
    int left = 2 * i + 1;
    if (left >= nextPosition) {
      return -1;
    } else {
      int right = 2 * i + 2;
      if (right >= nextPosition || values[left] > values[right]) {
        return left;
      } else {
        return right;
      }
    }
  }
}