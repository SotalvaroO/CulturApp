package piii.app.culturapp.models;

import java.util.ArrayList;

public class FCMResponse {

    private long multicast_id;
    private int success;
    private int failure;
    private int canonical_idd;
    ArrayList<Object> results = new ArrayList<Object>();

    public FCMResponse(long multicast_id, int success, int failure, int canonical_idd, ArrayList<Object> results) {
        this.multicast_id = multicast_id;
        this.success = success;
        this.failure = failure;
        this.canonical_idd = canonical_idd;
        this.results = results;
    }

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_idd() {
        return canonical_idd;
    }

    public void setCanonical_idd(int canonical_idd) {
        this.canonical_idd = canonical_idd;
    }

    public ArrayList<Object> getResults() {
        return results;
    }

    public void setResults(ArrayList<Object> results) {
        this.results = results;
    }
}
