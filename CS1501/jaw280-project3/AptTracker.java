import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class AptTracker{

    public static ApartmentPQ pq;
    public static Scanner input = new Scanner(System.in);

    public static void readExisting(ApartmentPQ h) throws FileNotFoundException {
        File file = new File("apartments.txt");
        Scanner reader = new Scanner(file);
        reader.useDelimiter(":|\\n");
        reader.nextLine();
        String address, aptNumber, city;
        int zip, price, area;

        while(reader.hasNext()){
            address = reader.next();
            aptNumber = reader.next();
            city = reader.next();
            zip = Integer.parseInt(reader.next());
            price = Integer.parseInt(reader.next());
            area = Integer.parseInt(reader.next());

            Apartment temp = new Apartment(address, aptNumber, city, zip, price, area);
            h.insert(temp);
        }
        reader.close();
    }

    public static int getAptIndex(String a, String n, int z) {
        String s = a + n + z;
        return s.hashCode();
    }

    public static void startDriver(){
        int choice = 0;
        while(choice != 8){
            System.out.println("\nApartment Rental Helper " +
                    "\nChoose an option:" +
                    "\n1) Add an apartment" +
                    "\n2) Update an apartment" +
                    "\n3) Remove an apartment" +
                    "\n4) Get lowest price" +
                    "\n5) Get highest square footage" +
                    "\n6) Get lowest price by city" +
                    "\n7) Get highest square footage by city" +
                    "\n8) Exit");

            try{
                choice = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException e){
                choice = 10;
                input.next();
            }
            switch(choice){
                case 1:
                    add();
                    break;
                case 2:
                    update();
                    break;
                case 3:
                    remove();
                    break;
                case 4:
                    getLowest();
                    break;
                case 5:
                    getHighest();
                    break;
                case 6:
                    getLowestByCity();
                    break;
                case 7:
                    getHighestByCity();
                    break;
                case 8:
                    System.out.println("\nGoodbye.");
                    break;
                default:
                    System.out.println("\nNot an option.");
                    break;
            }
        }

    }

    private static void add(){
        String address, aptNumber, city;
        int zip, price, area;

        System.out.println("Enter street address: ");
        address = input.nextLine();
        System.out.println("Enter apartment number: ");
        aptNumber = input.nextLine();
        System.out.println("Enter city: ");
        city = input.nextLine();
        System.out.println("Enter zip code: ");
        zip = input.nextInt();
        System.out.println("Enter price: ");
        price = input.nextInt();
        System.out.println("Enter square footage: ");
        area = input.nextInt();

        Apartment temp = new Apartment(address, aptNumber, city, zip, price, area);
        pq.insert(temp);
        System.out.println("\nAdded.");
    }

    private static void update(){
        String address, aptNumber;
        int zip, price;

        System.out.println("Enter street address: ");
        address = input.nextLine();
        System.out.println("Enter apartment number: ");
        aptNumber = input.nextLine();
        System.out.println("Enter zip code: ");
        zip = input.nextInt();
        System.out.println("Enter new price: ");
        price = input.nextInt();

        int index = getAptIndex(address, aptNumber, zip);
        pq.update(index, price);
        System.out.println("\nUpdated.");
    }

    private static void remove(){
        String address, aptNumber;
        int zip;

        System.out.println("Enter street address: ");
        address = input.nextLine();
        System.out.println("Enter apartment number: ");
        aptNumber = input.nextLine();
        System.out.println("Enter zip code: ");
        zip = input.nextInt();

        int index = getAptIndex(address, aptNumber, zip);
        pq.remove(index);
        System.out.println("\nRemoved.");
    }

    private static void getLowest(){
        Apartment apt = pq.getLowPrice();
        System.out.println("\nCheapest apartment: ");
        System.out.println(apt.toString());
    }

    private static void getHighest(){
        Apartment apt = pq.getHighArea();
        System.out.println("\nBiggest apartment: ");
        System.out.println(apt.toString());
    }

    private static void getLowestByCity(){
        System.out.println("Enter city: ");
        String city = input.nextLine();
        Apartment apt = pq.getLowByCity(city);
        System.out.println("\nCheapest apartment in " + city + ": ");
        System.out.println(apt.toString());
    }

    private static void getHighestByCity(){
        System.out.println("Enter city: ");
        String city = input.nextLine();
        Apartment apt = pq.getHighByCity(city);
        System.out.println("\nBiggest apartment in " + city + ": ");
        System.out.println(apt.toString());
    }

    public static void main(String[] args){
        pq = new ApartmentPQ();

        try{
            readExisting(pq);
        } catch (FileNotFoundException e){
            System.out.println("File not found.");
        }

        startDriver();
    }
}