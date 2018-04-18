
public class MergeSorter implements IntSorter{
	private int array[] = null;
	private int moves = 0;
	private long startTime = 0;
	private long endTime = 0;
	
	
	
	private void mergeSort(int[] arr, int[] temp, int start, int end) {
		if(start == end) return;
		
		int mid = (start+end)/2;
		mergeSort(arr, temp, start, mid);
		mergeSort(arr, temp, mid+1, end);
		merge(arr, temp, start, mid, end);
		
	}
	
	private void merge(int[] arr, int[] temp, int start, int mid, int end) {
		int start1 = start;
		int start2 = mid+1;
		int ndx = 0;
		
		while(start1<=mid && start2<=end) {
			if(array[start1] <= array[start2]) {
				temp[ndx] = array[start1];
				start1++;
			} else {
				temp[ndx] = array[start2];
				start2++;
			}
			ndx++;
		}
		
		while(start2<=end) {
			temp[ndx] = array[start2];
			ndx++;
			start2++;
		}
		
		while(start1<=mid){
			temp[ndx] = array[start1];
			ndx++;
			start1++;
		}
		
		moves += ndx;
		System.arraycopy(temp, 0, arr, start, ndx);
	}
	
	public void init(int[] arr) {
		this.array = arr;
		moves = 0;
	}
	
	public void sort() {
		startTime = System.nanoTime();
		
		int temp[] = new int[array.length];
		mergeSort(array, temp, 0, array.length-1);
		
		endTime = System.nanoTime();
	}
	
	public long getSortTime() {
		return endTime-startTime;
	}
	
	public int getMoves() {
		return moves;
	}
}
