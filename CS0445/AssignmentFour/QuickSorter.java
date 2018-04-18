import java.util.Scanner;

public class QuickSorter {

  public static void main(String[] args)  {
    int[] a;
    if (args.length > 0) {
      a = parseIntArray(args);
    } else {
      Scanner in = new Scanner(System.in);
      System.out.println("Enter a comma-separated list of integers to sort, or type random:");
      String line = in.nextLine();
      if (line.trim().equalsIgnoreCase("random")) {
        a = new int[25];
        for (int i = 0; i < a.length; i++) {
          a[i] = (int)(Math.random() * 100);
        }
      } else {
        a = parseIntArray(line.split(","));
      }
    }
    
    System.out.println("You entered:");
    writeArray(a);
    
    sort(a);
    
    System.out.println("The sorted array is: ");
    writeArray(a);
    System.out.println();
  }
  
  private static int[] parseIntArray(String[] values) {
    int[] result = new int[values.length];
    for (int i = 0; i < values.length; i++) {
      result[i] = Integer.parseInt(values[i].trim());
    }
    return result;
  }

  public static void sort(int[] a) {
    sort(a, 0, a.length);
  }

  private static void sort(int[] a, int begin, int end) {
    int middle = partition(a, begin, end);
    // assertion: value at middle is in the right place, everything to it's left is smaller (or 
    //            equal), everything to its right is larger.
    
    int frontSize = middle - begin;
    if (frontSize > 4) {
      // usually we'd use a larger size, but if we still have more than 4 values to sort, recurse.
      sort(a, begin, middle);
    } else if (frontSize > 1) {
      insertionSort(a, begin, middle);
      // if we 2, 3, or 4 values, we'll use insertion sort to sort them.
    }
    
    int backSize = end - middle - 1;
    if (backSize > 4) {
      // usually we'd use a larger size, but if we still have more than 4 values to sort, recurse.
      sort(a, middle + 1, end);
    } else if (backSize > 1) {
      // if we 2, 3, or 4 values, we'll use insertion sort to sort them.
      insertionSort(a, middle + 1, end);
    }
  }

  private static int partition(int[] a, int begin, int end) {
    System.out.println("At the beginning of partition, our array is:");
    writeArray(a, begin, end);
    // end is to the "right" of the last value in the array (i.e., end is array.length to start)

    int middle = (begin + end) / 2;
    System.out.println("Our begin is " + arrayValue(a, begin) + ", our middle is " + 
                       arrayValue(a, middle) + ", and our end is " + arrayValue(a, end - 1) + ".");

    System.out.println("Let's arrange our 3 values using median-of-three.");
    arrange(a, begin, middle, end - 1); // now we can be sure that a[begin] < a[middle] < a[end]
    
    System.out.println("After arranging begin, middle, and end our array is:");
    writeArray(a, begin, end);
    
    int middleValue = a[middle];
    System.out.println("Our middle value is " + arrayValue(a, middle) + ". " + 
                       "Swapping middle to near the end: ");
    swap(a, middle, end - 2);
    
    System.out.println("Now we move from the outsides in, and move values smaller than " + 
                      middleValue + " to the left side and larger values to the right side.");

    int leftSide = begin;    // start at begin index
    int rightSide = end - 3; // start at left of "middle" value (remember, it's near the end now)

    while (leftSide <= rightSide) {
      // this advances leftSide until we find something that doesn't belong.
      while (leftSide <= rightSide && a[leftSide] <= middleValue) {
        leftSide++;
      }

      // this advances rightSide until we find something that doesn't belong.
      while (leftSide <= rightSide && a[rightSide] > middleValue) {
        rightSide--;
      }
      
      if (leftSide < rightSide) {
        // at this point, we know we have something on both sides that doesn't belong. swap them.
        swap(a, leftSide, rightSide);
        leftSide++;
        rightSide--;
      }
    }

    System.out.println("Last, we move the middle back:");
    swap(a, leftSide, end - 2);
    System.out.println("The middle was put back at " + arrayValue(a, leftSide) + 
                       "; we know now it's in the right position.");
    System.out.println("(everything left) <= " + arrayValue(a, leftSide) + " < (everything right)");

    System.out.println("After this call to partition, we have this array:");
    writeArray(a, begin, end);
    System.out.println();

    return leftSide; // remember, leftSide is where middle ends up.
  }

  private static void arrange(int[] a, int x, int y, int z) {
    int min = x;
    if (a[y] < a[min]) {
      min = y;
    }
    if (a[z] < a[min]) {
      min = z;
    }
    swap(a, x, min);
    int max = y;
    if (a[z] > a[max]) {
      max = z;
    }
    swap(a, z, max);
  }

  private static void swap(int[] a, int x, int y) {
    if (x != y) System.out.println("> swapping " + arrayValue(a, x) + " with " + arrayValue(a, y) + ".");
    int temp = a[x];
    a[x] = a[y];
    a[y] = temp;
  }

  private static void insertionSort(int[] a, int begin, int end) {
    System.out.println("Insertion sorting this array:");
    writeArray(a, begin, end);
    for (int i = begin + 1; i < end; i++) {
      int k = i;
      while (k > begin && a[k] < a[k - 1]) {
        swap(a, k, k - 1);
        k--;
      }
    }
    System.out.println("to:");
    writeArray(a, begin, end);
    System.out.println();
  }
  
  private static void writeArray(int[] a) {
    writeArray(a, 0, a.length);
  }

  private static void writeArray(int[] a, int begin, int end) {
    System.out.print("| ");
    for (int i = 0; i < a.length; i++) {
      if (i < begin || i >= end) {
        System.out.print("x | ");
      } else {
        System.out.print(arrayValue(a, i) + " | ");
      }
    }
    System.out.println("\n");
  }

  private static String arrayValue(int[] a, int index) {
    return "[" + index + "]" + a[index];
  }
}