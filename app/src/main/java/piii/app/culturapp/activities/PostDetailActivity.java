package piii.app.culturapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import piii.app.culturapp.R;
import piii.app.culturapp.adapters.SliderAdapter;
import piii.app.culturapp.models.SliderItem;
import piii.app.culturapp.providers.PostProvider;

public class PostDetailActivity extends AppCompatActivity {

    SliderView mSliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItems = new ArrayList<>();
    String mExtraPostId;
    PostProvider mPostProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        mSliderView = findViewById(R.id.imageSlider);
        mPostProvider = new PostProvider();

        mExtraPostId = getIntent().getStringExtra("id");

        getPost();
    }

    private void instanceSlide(){
        mSliderAdapter = new SliderAdapter(PostDetailActivity.this, mSliderItems);
        mSliderView.setSliderAdapter(mSliderAdapter);
        mSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        mSliderView.setIndicatorSelectedColor(Color.WHITE);
        mSliderView.setIndicatorUnselectedColor(Color.GRAY);
        mSliderView.setScrollTimeInSec(3);
        mSliderView.setAutoCycle(true);
        mSliderView.startAutoCycle();
    }

    private void getPost() {
        mPostProvider.getPostById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("image1")) {
                        String image1 = documentSnapshot.getString("image1");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(image1);
                        mSliderItems.add(item);
                    }
                    if (documentSnapshot.contains("image2")) {
                        String image2 = documentSnapshot.getString("image2");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(image2);
                        mSliderItems.add(item);
                    }
                    instanceSlide();
                }
            }
        });
    }
}