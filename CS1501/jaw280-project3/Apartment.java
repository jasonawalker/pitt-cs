public class Apartment{
    private String address;
    private String aptNumber;
    private String city;
    private int zip;
    private int price;
    private int area;

    public Apartment(String a, String n, String c, int z, int p, int ar){
        address = a;
        aptNumber = n;
        city = c;
        zip = z;
        price = p;
        area = ar;
    }

    public String getAddress() { return this.address; }
    public String getAptNumber() { return this.aptNumber; }
    public String getCity() { return this.city; }
    public int getZip() { return this.zip; }
    public int getPrice() { return this.price; }
    public int getArea() { return this.area; }

    public int getIndex() {
        String s = address + aptNumber + zip;
        return s.hashCode();
//        return Integer.parseInt(s.replaceAll("[\\D]", ""));
    }

    public void setAddress(String a) { this.address = a; }
    public void setAptNumber(String n) { this.aptNumber = n; }
    public void setCity(String c) { this.city = c; }
    public void setZip(int z) { this.zip = z; }
    public void setPrice(int p) { this.price = p; }
    public void setArea(int ar) { this.area = ar; }

    @java.lang.Override
    public String toString() {
        return "Address: " + address + "\n" +
                "Apartment Number: " + aptNumber + "\n" +
                "City: " + city + "\n" +
                "Zip Code: " + zip + "\n" +
                "Price: " + price + "\n" +
                "Square Footage: " + area + "\n";
    }
}