public class BubbleSorter implements IntSorter {

  private int[] array = null;
  private int moves = 0;
  private long startTime = 0;
  private long endTime = 0;

  public void init(int[] a) {
    this.array = a;
    moves = 0;
  }

  public void sort() {
    startTime = System.nanoTime();
    boolean done = false;
    while (!done) {
      done = true;
      for (int i = 0; i < array.length - 1; i++) {
        if (array[i] > array[i + 1]) {
          swap(array, i, i + 1);
          done = false;
        }
      }
    }
    endTime = System.nanoTime();
  }
  
  private void swap(int[] values, int p1, int p2) {
    int temp = values[p1];
    values[p1] = values[p2];
    values[p2] = temp;
    moves++;
  }

  public long getSortTime() {
    return endTime - startTime;
  }

  public int getMoves() {
    return moves;
  }
}