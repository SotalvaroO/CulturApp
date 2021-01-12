package piii.app.culturapp.models;

public class Post {
    private String id;
    private String title;
    private String description;
    private String image1;
    private String image2;
    private String idUser;
    private long timestamp;
    private String latitude;
    private String longitude;
    //Falta implementar la ubicación

    public Post() {

    }

    public Post(String id, String title, String description, String image1, String image2, String idUser, long timestamp, String latitude, String longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.idUser = idUser;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude=longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
