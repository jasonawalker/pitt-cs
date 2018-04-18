
public class SelectionSorter implements IntSorter{
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
		
		for(int i=0; i< array.length-1; i++) {
			int min = i;
			for(int j = i+1; j<array.length; j++) {
				if(array[j] < array[min]) {
					min = j;
				}
			}
			swap(array, i, min);
		}
		
		endTime = System.nanoTime();
	}
	
	public long getSortTime() {
		return endTime-startTime;
	}
	
	public int getMoves() {
		return moves;
	}
}
