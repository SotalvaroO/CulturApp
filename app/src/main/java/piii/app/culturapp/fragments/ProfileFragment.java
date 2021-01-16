package piii.app.culturapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import piii.app.culturapp.R;
import piii.app.culturapp.activities.EditProfileActivity;
import piii.app.culturapp.adapters.MyPostsAdapter;
import piii.app.culturapp.adapters.PostsAdapter;
import piii.app.culturapp.models.Post;
import piii.app.culturapp.models.User;
import piii.app.culturapp.providers.AuthProvider;
import piii.app.culturapp.providers.PostProvider;
import piii.app.culturapp.providers.UserProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout mLinearLayoutEditProfile;

    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;
    TextView mTextViewPostExist;
    RecyclerView mRecyclerView;

    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;

    View mView;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    MyPostsAdapter mAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEditProfile);
        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfile();
            }
        });

        mTextViewEmail = mView.findViewById(R.id.textViewProfileEmail);
        mTextViewUsername = mView.findViewById(R.id.textViewProfileUsername);
        mTextViewPhone = mView.findViewById(R.id.textViewProfilePhone);
        mTextViewPostExist = mView.findViewById(R.id.textViewPostExist);
        mTextViewPostNumber = mView.findViewById(R.id.textViewProfilePostNumber);
        mRecyclerView = mView.findViewById(R.id.recyclerViewMyPost);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mImageViewCover = mView.findViewById(R.id.imageViewProfileCover);
        mCircleImageViewProfile = mView.findViewById(R.id.circleImageViewProfile);

        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
        checkIfPostExist();
        getUser();
        getPostNumber();
        return mView;
    }

    private void checkIfPostExist() {
        mPostProvider.getPostByUser(mAuthProvider.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                int numberPost= queryDocumentSnapshots.size();
                if (numberPost>0){
                    mTextViewPostExist.setText("Publicaciones");
                    mTextViewPostExist.setTextColor(Color.BLACK);
                }else {
                    mTextViewPostExist.setText("No hay publicaciones");
                    mTextViewPostExist.setTextColor(Color.GRAY);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByUser(mAuthProvider.getUid());
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        mAdapter = new MyPostsAdapter(options, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getPostNumber() {
        mPostProvider.getPostByUser(mAuthProvider.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                Log.e("Error", "Firebase Ex ", error);
                int numberPost = queryDocumentSnapshots.size();
                mTextViewPostNumber.setText(String.valueOf(numberPost));
            }
        });
    }

    private void getUser() {
        mUserProvider.getRealTimeUsers(mAuthProvider.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Log.e("Error", "Firebase Ex ", error);
                if (documentSnapshot.exists() && documentSnapshot != null) {
                    if (documentSnapshot.contains("email")) {
                        String email = documentSnapshot.getString("email");
                        mTextViewEmail.setText(email);
                    }
                    if (documentSnapshot.contains("phone")) {
                        String phone = documentSnapshot.getString("phone");
                        mTextViewPhone.setText(phone);
                    }
                    if (documentSnapshot.contains("username")) {
                        String username = documentSnapshot.getString("username");
                        mTextViewUsername.setText(username);
                    }
                    if (documentSnapshot.contains("image_profile")) {
                        String imageProfile = documentSnapshot.getString("image_profile");
                        if (imageProfile != null) {
                            if (!imageProfile.isEmpty()) {
                                Picasso.with(getContext()).load(imageProfile).into(mCircleImageViewProfile);
                            }
                        }

                    }
                    if (documentSnapshot.contains("image_cover")) {
                        String imageCover = documentSnapshot.getString("image_cover");
                        if (imageCover != null) {
                            if (!imageCover.isEmpty()) {
                                Picasso.with(getContext()).load(imageCover).into(mImageViewCover);
                            }
                        }

                    }

                }
            }
        });
    }
}