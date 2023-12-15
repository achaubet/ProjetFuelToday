package fr.univpau.fueltoday;

public class Station {
    public int id;
    public double latitude;
    public double longitude;
    public String cp;
    public String pop;
    public String address;
    public String city;
    public Station(int id, double latitude, double longitude, String cp, String pop, String address, String city) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cp = cp;
        this.pop = pop;
        this.address = address;
        this.city = city;
    }

}
