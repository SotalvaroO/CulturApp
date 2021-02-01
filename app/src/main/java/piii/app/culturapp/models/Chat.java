package piii.app.culturapp.models;

import java.util.ArrayList;

public class Chat {

    private String id;
    private boolean isWriting;
    private long timestamp;
    private String idUser1;
    private String idUser2;
    private ArrayList<String> ids;

    public Chat() {

    }

    public Chat(String id, boolean isWriting, long timestamp, String idUser1, String idUser2, ArrayList<String> ids) {
        this.id = id;
        this.isWriting = isWriting;
        this.timestamp = timestamp;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.ids = ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }
}
