import java.util.Comparator;

public class PriceComparator implements Comparator<Apartment> {
    public int compare(Apartment apt1, Apartment apt2){
        return apt1.getPrice() - apt2.getPrice();
    }
}
