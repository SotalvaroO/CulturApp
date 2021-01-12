package piii.app.culturapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import piii.app.culturapp.R;
import piii.app.culturapp.activities.PostDetailActivity;
import piii.app.culturapp.models.Like;
import piii.app.culturapp.models.Post;
import piii.app.culturapp.providers.AuthProvider;
import piii.app.culturapp.providers.LikeProvider;
import piii.app.culturapp.providers.PostProvider;
import piii.app.culturapp.providers.UserProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {

    Context context;
    PostProvider mPostProvider;
    UserProvider mUserProvider;
    LikeProvider mLikeProvider;
    AuthProvider mAuthProvider;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
        mPostProvider = new PostProvider();
        mUserProvider = new UserProvider();
        mLikeProvider = new LikeProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post post) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String postId = document.getId();
        holder.textViewTitle.setText(post.getTitle());
        holder.textViewDescription.setText(post.getDescription());

        if (post.getImage1() != null) {
            if (!post.getImage1().isEmpty()) {
                Picasso.with(context).load(post.getImage1()).into(holder.imageViewPost);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDetailActivity = new Intent(context, PostDetailActivity.class);
                goToDetailActivity.putExtra("id", postId);
                context.startActivity(goToDetailActivity);
            }
        });

        holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like like = new Like();
                like.setIdUser(mAuthProvider.getUid());
                like.setIdPost(postId);
                like.setTimestamp(new Date().getTime());
                like(like, holder);
            }
        });
        getUserInfo(post.getIdUser(), holder);
        getLikesNumberByPost(postId, holder);
        checkLike(postId, mAuthProvider.getUid(), holder);

    }

    private void getLikesNumberByPost(String idPost, final ViewHolder holder) {
        mLikeProvider.getLiekesByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                int numberLikes = queryDocumentSnapshots.size();
                holder.textViewLike.setText(String.valueOf(numberLikes + " Me Gusta"));
            }
        });
    }

    private void like(final Like like, final ViewHolder holder) {
        mLikeProvider.getLikeByPostAndUser(like.getIdPost(), mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0) {
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.imageViewLike.setImageResource(R.drawable.ic_baseline_like_96_gray);
                    mLikeProvider.delete(idLike);
                } else {
                    holder.imageViewLike.setImageResource(R.drawable.ic_baseline_like_96_blue);
                    mLikeProvider.create(like);
                }
            }
        });
    }

    private void checkLike(String idPost, String idUser, final ViewHolder holder) {
        mLikeProvider.getLikeByPostAndUser(idPost, idUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots != null) {
                    int numberDocuments = queryDocumentSnapshots.size();
                    if (numberDocuments > 0) {
                        holder.imageViewLike.setImageResource(R.drawable.ic_baseline_like_96_blue);
                    } else {
                        holder.imageViewLike.setImageResource(R.drawable.ic_baseline_like_96_gray);
                    }
                } else {
                    Log.e("Error", "Firebase Ex ", error);
                }
            }
        });
    }

    private void getUserInfo(String idUser, final ViewHolder holder) {
        mUserProvider.getUsers(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists() && documentSnapshot != null) {
                    if (documentSnapshot.contains("username")) {
                        String username = documentSnapshot.getString("username");
                        holder.textViewUsername.setText("Por: " + username);
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewLike;
        TextView textViewUsername;
        ImageView imageViewPost;
        ImageView imageViewLike;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitlePostCard);
            textViewDescription = view.findViewById(R.id.textViewDescriptionPostCard);
            textViewLike = view.findViewById(R.id.textViewLike);
            textViewUsername = view.findViewById(R.id.textViewUsernamePostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            imageViewLike = view.findViewById(R.id.imageViewLike);
            viewHolder = view;
        }
    }

}
