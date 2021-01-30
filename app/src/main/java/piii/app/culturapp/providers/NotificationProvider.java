package piii.app.culturapp.providers;

import piii.app.culturapp.models.FCMBody;
import piii.app.culturapp.models.FCMResponse;
import piii.app.culturapp.retrofit.IFCMApi;
import piii.app.culturapp.retrofit.RetrofitClient;
import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {

    }

    public Call<FCMResponse> sendNotification(FCMBody body) {
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }

}
