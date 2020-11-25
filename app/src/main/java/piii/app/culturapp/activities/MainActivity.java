package piii.app.culturapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import piii.app.culturapp.R;
import piii.app.culturapp.fragments.ChatFragment;
import piii.app.culturapp.fragments.FilterFragment;
import piii.app.culturapp.fragments.HomeFragment;
import piii.app.culturapp.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    TextView textViewHelloWorld;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        textViewHelloWorld = findViewById(R.id.textViewHelloWorld);
        openFragment(HomeFragment.newInstance("", ""));

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        textViewHelloWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewChange = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(viewChange);
            }
        });
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.itemHome:
                            openFragment(HomeFragment.newInstance("", ""));
                            return true;
                        case R.id.itemFilters:
                            openFragment(FilterFragment.newInstance("", ""));
                            return true;
                        case R.id.itemChat:
                            openFragment(ChatFragment.newInstance("", ""));
                            return true;
                        case R.id.itemProfile:
                            openFragment(ProfileFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}