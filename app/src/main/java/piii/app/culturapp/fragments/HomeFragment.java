package piii.app.culturapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

import piii.app.culturapp.R;
import piii.app.culturapp.activities.LoginActivity;
import piii.app.culturapp.activities.PostActivity;
import piii.app.culturapp.adapters.PostsAdapter;
import piii.app.culturapp.models.Post;
import piii.app.culturapp.providers.AuthProvider;
import piii.app.culturapp.providers.PostProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View mView;
    FloatingActionButton mFab;
    RecyclerView mRecyclerView;
    PostProvider mPostProvider;
    PostsAdapter mPostsAdapter;
    AuthProvider mAuthProvider;
    PostsAdapter mPostAdapterSearch;
    MaterialSearchBar mSearchBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        setHasOptionsMenu(true);

        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mFab = mView.findViewById(R.id.floatingActionButtonAdd);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost();
            }
        });
        mRecyclerView = mView.findViewById(R.id.recyclerViewHome);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mSearchBar = mView.findViewById(R.id.searchBar);

        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        mSearchBar.setOnSearchActionListener(this);
        mSearchBar.inflateMenu(R.menu.main_menu);
        mSearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    logout();
                }
                return true;
            }
        });

        return mView;
    }

    private void searchByTitle(String title){
        Query query = mPostProvider.getPostByTitle(title);
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        mPostAdapterSearch = new PostsAdapter(options, getContext());
        mPostAdapterSearch.notifyDataSetChanged();
        mRecyclerView.setAdapter(mPostAdapterSearch);
        mPostAdapterSearch.startListening();
    }

    private void getAllPost(){
        Query query = mPostProvider.getAll();
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        mPostsAdapter = new PostsAdapter(options, getContext());
        mPostsAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mPostsAdapter);
        mPostsAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllPost();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPostsAdapter.stopListening();
        if (mPostAdapterSearch!=null){
            mPostAdapterSearch.stopListening();
        }
    }

    private void goToPost() {
        Intent goToPost = new Intent(getContext(), PostActivity.class);
        startActivity(goToPost);
    }

    private void logout() {
        mAuthProvider.logout();
        Intent goToLogin = new Intent(getContext(), LoginActivity.class);
        goToLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToLogin);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getAllPost();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByTitle(text.toString().toLowerCase());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}