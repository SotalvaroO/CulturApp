package piii.app.culturapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import piii.app.culturapp.R;
import piii.app.culturapp.providers.AuthProvider;
import piii.app.culturapp.providers.PostProvider;
import piii.app.culturapp.providers.UserProvider;

public class UserProfileActivity extends AppCompatActivity {

    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;

    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;

    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    CircularImageView mCircularImageView;

    String mExtraIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mTextViewEmail = findViewById(R.id.textViewProfileEmail);
        mTextViewUsername = findViewById(R.id.textViewProfileUsername);
        mTextViewPhone = findViewById(R.id.textViewProfilePhone);
        mTextViewPostNumber = findViewById(R.id.textViewProfilePostNumber);

        mImageViewCover = findViewById(R.id.imageViewProfileCover);
        mCircleImageViewProfile = findViewById(R.id.circleImageViewProfile);
        mCircularImageView = findViewById(R.id.circularReturnUserProfileButton);

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

        getUser();
        getPostNumber();

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