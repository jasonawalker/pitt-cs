import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArrayTest {
  
  int array[] = createArray(2000);
  int sortCopy[] ;
  IntSorter sorter;
	
  public static int[] createArray(int size) {
    int[] result = new int[size];
    for (int i = 0; i < result.length; i++) {
      result[i] = (int)(Math.random() * 1000);
    }
    return result;
  }
  
  public static boolean isSorted(int[] array) {
    for (int i = 1; i < array.length; i++) {
      if (array[i - 1] > array[i]) {
        return false;
      }
    }
    return true;
  }
  
  @Before
  public void createTemp() {
	  sortCopy = Arrays.copyOf(array, array.length);
  }
  
  
  @Test
  public void testInsertionSort() {
    sorter = new InsertionSorter();
    sorter.init(sortCopy);
    sorter.sort();
    System.out.println("\nINSERTION");
    assertTrue(isSorted(sortCopy));
  }
  
  @Test
  public void testSelectionSort() {
    sorter = new SelectionSorter();
    sorter.init(sortCopy);
    sorter.sort();
    System.out.println("\nSELECTION");
    assertTrue(isSorted(sortCopy));
  }
  
  @Test
  public void testBubbleSort() {
	    sorter = new BubbleSorter();
	    sorter.init(sortCopy);
	    sorter.sort();
	    System.out.println("\nBUBBLE");
	    assertTrue(isSorted(sortCopy));
	  }
  
  @Test
  public void testMergeSort() {
    sorter = new MergeSorter();
    sorter.init(sortCopy);
    sorter.sort();
    System.out.println("\nMERGE");
    assertTrue(isSorted(sortCopy));
  }
  
  @After
  public void getValues() {
	  System.out.println("Moves: " + sorter.getMoves());
	  System.out.println("Time: " + sorter.getSortTime() + " ns");
  }
}