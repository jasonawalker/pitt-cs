import java.util.Comparator;

public class AreaComparator implements Comparator<Apartment> {
    public int compare(Apartment apt1, Apartment apt2){
        return apt2.getArea() - apt1.getArea();
    }
}
