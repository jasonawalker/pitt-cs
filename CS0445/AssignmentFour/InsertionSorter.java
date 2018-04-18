
public class InsertionSorter implements IntSorter{
	private int array[] = null;
	private int moves = 0;
	private long startTime = 0;
	private long endTime = 0;
	
	private void swap(int[] arr, int v1, int v2) {
		int temp = arr[v1];
		arr[v1] = arr[v2];
		arr[v2] = temp;
		moves++;
	}
	
	public void init(int[] arr) {
		this.array = arr;
		moves = 0;
	}
	
	public void sort() {
		startTime = System.nanoTime();
		
		for(int i =0; i<array.length;i++) {
			for(int j = i; j >0; j--) {
				if(array[j] < array[j-1]) {
					swap(array, j-1, j);
				}
			}
		}
		
//		for(int i=1; i<=array.length; i++) {
//			int k = i-1;
//			while(k >= 1 && array[k-1] > array[k]) {
//				swap(array, k-1, k);
//				k--;
//			}
//		}
		
		endTime = System.nanoTime();
	}
	
	public long getSortTime() {
		return endTime-startTime;
	}
	
	public int getMoves() {
		return moves;
	}
}
