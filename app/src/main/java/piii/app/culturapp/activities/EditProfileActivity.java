package piii.app.culturapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import piii.app.culturapp.R;

public class EditProfileActivity extends AppCompatActivity {

    CircularImageView mCircularImageView;
    CircleImageView mCircleImageViewProfile;
    ImageView mImageViewCover;
    TextInputEditText textInputUsername;
    TextInputEditText textInputPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mCircularImageView = findViewById(R.id.circleImageBack);
        mCircleImageViewProfile = findViewById(R.id.circleImageProfile);
        mImageViewCover = findViewById(R.id.imageViewCover);
        textInputUsername = findViewById(R.id.textInputUsername);
        textInputPhone = findViewById(R.id.textInputPhone);

        mCircularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}