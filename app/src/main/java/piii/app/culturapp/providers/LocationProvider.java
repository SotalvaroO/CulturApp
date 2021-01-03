package piii.app.culturapp.providers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LocationProvider {

    android.location.Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    CollectionReference mCollection;

    public LocationProvider(Context context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        mCollection= FirebaseFirestore.getInstance().collection("Location");
    }

    public Task<android.location.Location> getCurrentLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return fusedLocationProviderClient.getLastLocation();
    }

    public Task<Void> saveLocation(piii.app.culturapp.models.Location location){
        return mCollection.document().set(location);
    }

}
