package piii.app.culturapp.models;

public class Location {
    double latitude;
    double altitude;
    double longitude;

    public Location() {

    }

    public Location( double latitude, double altitude, double longitude) {
        this.latitude = latitude;
        this.altitude = altitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
