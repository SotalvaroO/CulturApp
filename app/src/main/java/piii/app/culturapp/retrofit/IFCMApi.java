package piii.app.culturapp.retrofit;

import piii.app.culturapp.models.FCMBody;
import piii.app.culturapp.models.FCMResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAePoJsVI:APA91bGufKpiCKhuDB3CVVj1woYJrV_3cjz9wMbw1CCUfN4T19rUd4sjNT4n6OKy_soa8Ny1Q4al0rS6--Ud4mYcRXveZjgGmhT4qX1NaqvPGjvhlJiN9wCCwojKrn6391gjZ6OEdKtW"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}
