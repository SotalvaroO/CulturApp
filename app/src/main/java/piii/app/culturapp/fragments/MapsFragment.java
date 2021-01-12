package piii.app.culturapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import piii.app.culturapp.R;
import piii.app.culturapp.activities.LoginActivity;
import piii.app.culturapp.models.Post;
import piii.app.culturapp.providers.AuthProvider;
import piii.app.culturapp.providers.PostProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    View mView;
    private String mParam1;
    private String mParam2;

    private static final int REQUEST_CODE = 101;

    Location currentLocation;

    GoogleMap gMap;

    Toolbar mToolbar;

    Post post = new Post();

    PostProvider mPostProvider;

    AuthProvider mAuthProvider;

    List<String> list = new ArrayList<>();

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_maps, container, false);

        // Inflate the layout for this fragment
        mToolbar = mView.findViewById(R.id.toolbar);
        mAuthProvider = new AuthProvider();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mapa");
        setHasOptionsMenu(true);

        mPostProvider = new PostProvider();


        return mView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logout();
        }
        return true;
    }

    private void logout() {
        mAuthProvider.logout();
        Intent goToLogin = new Intent(getContext(), LoginActivity.class);
        goToLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToLogin);
    }

    /*public List<String> getDocuments() {
        mPostProvider.getDocuments().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                }
            }
        });
        Log.d("topo", "getDocuments: " + list.get(0));
        return list;
    }*/


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        mPostProvider.getDocuments().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    for (String mDocument : list) {

                        mPostProvider.getObject(mDocument).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    post.setLatitude(documentSnapshot.getString("latitude"));
                                    post.setLongitude(documentSnapshot.getString("longitude"));

                                    LatLng latLng = new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongitude()));
                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Estoy acá");
                                    gMap.addMarker(markerOptions);
                                }else {
                                    Log.e("Error", "Firebase Ex ", error);
                                }
                            }
                        });
                    }
                }
            }
        });
        LatLng coord = new LatLng(6.175339871821172, -75.4078373002132);
        gMap.animateCamera(CameraUpdateFactory.newLatLng(coord));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 10));
    }

}