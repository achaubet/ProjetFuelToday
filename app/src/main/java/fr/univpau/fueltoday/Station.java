package fr.univpau.fueltoday;

import java.util.Set;

public class Station {
    public int id;
    public double latitude;
    public double longitude;
    public String cp;
    public String pop;
    public String address;
    public String city;
    public double sp95_prix;
    public double e10_prix;
    public double sp98_prix;
    public double gasoil_prix;
    public double gplc_prix;
    public double e85_prix;
    public Set<String> services;
    public Station(int id, double latitude, double longitude, String cp, String pop, String address, String city, double sp95_prix, double e10_prix, double sp98_prix, double gasoil_prix, double gplc_prix, double e85_prix, Set<String> services) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cp = cp;
        this.pop = pop;
        this.address = address;
        this.city = city;
        this.sp95_prix = sp95_prix;
        this.e10_prix = e10_prix;
        this.sp98_prix = sp98_prix;
        this.gasoil_prix = gasoil_prix;
        this.gplc_prix = gplc_prix;
        this.e85_prix = e85_prix;
        this.services = services;
    }

}
