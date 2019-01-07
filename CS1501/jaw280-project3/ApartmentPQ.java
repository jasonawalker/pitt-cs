import java.util.Map;
import java.util.HashMap;

public class ApartmentPQ {
    private Heap globalPQ;
    private Map<String, Heap> cityMap;

    public Heap getHeap(){ return globalPQ; }
    public Map<String, Heap> getCityMap(){ return cityMap; }

    public ApartmentPQ() {
        globalPQ = new Heap();
        cityMap = new HashMap<>();
    }

    public void insert(Apartment apt){
        globalPQ.insert(apt);
        String city = apt.getCity();
        if(!(cityMap.containsKey(city))){
            cityMap.put(city, new Heap());
        }

        cityMap.get(city).insert(apt);
    }

    public void update(int key, int price){
        Apartment curr = globalPQ.get(key);
        globalPQ.update(key, price);
        Heap cityHeap = cityMap.get(curr.getCity());
        cityHeap.update(key,price);
    }

    public void remove(int key){
        Apartment curr = globalPQ.get(key);
        globalPQ.remove(key);
        Heap cityHeap = cityMap.get(curr.getCity());
        cityHeap.remove(key);


        if(cityHeap.isEmpty()){
            cityMap.remove(curr.getCity());
        }
    }

    public Apartment getLowPrice(){
        return globalPQ.min();
    }

    public Apartment getHighArea(){
        return globalPQ.max();
    }

    public Apartment getLowByCity(String city){
        Heap temp = cityMap.get(city);
        return temp.min();
    }

    public Apartment getHighByCity(String city){
        Heap temp = cityMap.get(city);
        return temp.max();
    }


}
