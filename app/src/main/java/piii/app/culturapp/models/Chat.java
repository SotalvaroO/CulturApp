package piii.app.culturapp.models;

public class Chat {

    private boolean isWriting;
    private long timestamp;
    private String idUser1;
    private String idUser2;

    public Chat() {

    }

    public Chat(boolean isWriting, long timestamp, String idUser1, String idUser2) {
        this.isWriting = isWriting;
        this.timestamp = timestamp;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
    }

    public boolean isWriting() {
        return isWriting;
    }

    public void setWriting(boolean writing) {
        isWriting = writing;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }
}
