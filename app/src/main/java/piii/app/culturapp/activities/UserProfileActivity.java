package piii.app.culturapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import piii.app.culturapp.R;
import piii.app.culturapp.adapters.MyPostsAdapter;
import piii.app.culturapp.models.Post;
import piii.app.culturapp.providers.AuthProvider;
import piii.app.culturapp.providers.PostProvider;
import piii.app.culturapp.providers.UserProvider;

public class UserProfileActivity extends AppCompatActivity {

    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;
    TextView mTextViewPostExist;

    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;

    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    CircularImageView mCircularImageView;

    RecyclerView mRecyclerView;
    MyPostsAdapter mAdapter;

    String mExtraIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mTextViewEmail = findViewById(R.id.textViewProfileEmail);
        mTextViewUsername = findViewById(R.id.textViewProfileUsername);
        mTextViewPhone = findViewById(R.id.textViewProfilePhone);
        mTextViewPostNumber = findViewById(R.id.textViewProfilePostNumber);
        mTextViewPostExist = findViewById(R.id.textViewPostExist);

        mImageViewCover = findViewById(R.id.imageViewProfileCover);
        mCircleImageViewProfile = findViewById(R.id.circleImageViewProfile);
        mCircularImageView = findViewById(R.id.circularReturnUserProfileButton);

        mRecyclerView = findViewById(R.id.recyclerViewMyPost);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mCircularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        mExtraIdUser = getIntent().getStringExtra("idUser");

        checkIfPostExist();
        getUser();
        getPostNumber();

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByUser(mExtraIdUser);
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        mAdapter = new MyPostsAdapter(options, UserProfileActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void checkIfPostExist() {
        mPostProvider.getPostByUser(mExtraIdUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void getPostNumber() {
        mPostProvider.getPostByUser(mExtraIdUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                int numberPost= queryDocumentSnapshots.size();
                mTextViewPostNumber .setText(String.valueOf(numberPost));
            }
        });
    }

    private void getUser() {
        mUserProvider.getRealTimeUsers(mExtraIdUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
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
                                Picasso.with(UserProfileActivity.this).load(imageProfile).into(mCircleImageViewProfile);
                            }
                        }

                    }
                    if (documentSnapshot.contains("image_cover")) {
                        String imageCover = documentSnapshot.getString("image_cover");
                        if (imageCover != null) {
                            if (!imageCover.isEmpty()) {
                                Picasso.with(UserProfileActivity.this).load(imageCover).into(mImageViewCover);
                            }
                        }

                    }

                }
            }
        });
    }

}