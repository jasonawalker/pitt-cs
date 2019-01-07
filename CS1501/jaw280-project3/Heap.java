import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class Heap {
    private Apartment[] minPQ;
    private Apartment[] maxPQ;
    private int size;

    private Map<Integer, int[]> map;

    private Comparator minComparator;
    private Comparator maxComaprator;

    public Heap(){
        minPQ = new Apartment[2];
        maxPQ = new Apartment[2];
        size = 0;
        minComparator = new PriceComparator();
        maxComaprator = new AreaComparator();
        map = new HashMap<>();
    }

    public boolean isEmpty(){ return size==0; }
    public int getSize(){ return this.size; }
    public Map<Integer, int[]> getMap(){ return this.map; }
    public Apartment[] getMinPQ(){ return this.minPQ; }
    public Apartment[] getMaxPQ(){ return this.maxPQ; }
    public Apartment max(){ return maxPQ[1]; }
    public Apartment min(){ return minPQ[1]; }

    public Apartment get(int key){
        return minPQ[map.get(key)[0]];
    }

    public void insert(Apartment apt){
        if(size == minPQ.length-1) resize(2*minPQ.length);

        size++;
        int key = apt.getIndex();
        int[] values = {size, size};
        map.put(key,values);
        minPQ[size] = apt;
        maxPQ[size] = apt;
        swim(size);
    }

    public void remove(int key){
        int minIndex = map.get(key)[0];
        int maxIndex = map.get(key)[1];
        swap(true, minIndex, size);
        swap(false, maxIndex, size);
        size--;
        sink(minIndex, true);
        sink(maxIndex, false);
        minPQ[size+1] = null;
        maxPQ[size+1] = null;
        map.remove(key);
    }

    public void update(int key, int price){
        int index = map.get(key)[0];
        Apartment parent = minPQ[index/2];
        minPQ[index].setPrice(price);
        if(!(index == 1) && price < parent.getPrice()){
            swim(index);
        } else {
            sink(index, true);
        }
    }

    private void resize(int newSize){
        Apartment[] tempMin = new Apartment[newSize];
        Apartment[] tempMax = new Apartment[newSize];

        for(int i = 1; i<=size; i++){
            tempMin[i] = minPQ[i];
            tempMax[i] = maxPQ[i];
        }

        minPQ = tempMin;
        maxPQ = tempMax;
    }

    private void swim(int ndx){
        int temp = ndx;
        while(ndx > 1 && greater(ndx/2, ndx)){
            swap(true, ndx, ndx/2);
            ndx = ndx/2;
        }
        ndx = temp;
        while(ndx > 1 && less(ndx/2, ndx)){
            swap(false, ndx, ndx/2);
            ndx = ndx/2;
        }

    }

    private void sink(int ndx, boolean minOnly){
        if(minOnly){
            while(2*ndx <= size){
                int i = 2*ndx;
                if (i < size && greater(i, i+1)) i++;
                if(!greater(ndx, i)) break;
                swap(true, ndx, i);
                ndx = i;
            }
        } else {
            while(2*ndx <= size){
                int i = 2*ndx;
                if (i < size && less(i, i+1)) i++;
                if(!less(ndx, i)) break;
                swap(false, ndx, i);
                ndx = i;
            }
        }
    }

    private void swap(boolean min, int i, int j){
        if(min){
            Apartment ap1 = minPQ[i];
            Apartment ap2 = minPQ[j];
            int ap1Index = ap1.getIndex();
            int ap2Index = ap2.getIndex();

            int[] temp1 = {j, map.get(ap1Index)[1]};
            int[] temp2 = {i, map.get(ap2Index)[1]};

            map.replace(ap1Index, temp1);
            map.replace(ap2Index, temp2);

            minPQ[i] = ap2;
            minPQ[j] = ap1;
        } else {
            Apartment ap1 = maxPQ[i];
            Apartment ap2 = maxPQ[j];
            int ap1Index = ap1.getIndex();
            int ap2Index = ap2.getIndex();

            int[] temp1 = {map.get(ap1Index)[0], j};
            int[] temp2 = {map.get(ap2Index)[0], i};

            map.replace(ap1Index, temp1);
            map.replace(ap2Index, temp2);

            maxPQ[i] = ap2;
            maxPQ[j] = ap1;
        }
    }

    private boolean less(int i, int j){
        return maxComaprator.compare(maxPQ[i], maxPQ[j]) > 0;
    }

    private boolean greater(int i, int j){
        return minComparator.compare(minPQ[i], minPQ[j]) > 0;
    }
}
