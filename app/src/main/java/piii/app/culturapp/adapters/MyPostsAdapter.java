package piii.app.culturapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import piii.app.culturapp.R;
import piii.app.culturapp.activities.PostDetailActivity;
import piii.app.culturapp.models.Like;
import piii.app.culturapp.models.Post;
import piii.app.culturapp.providers.AuthProvider;
import piii.app.culturapp.providers.LikeProvider;
import piii.app.culturapp.providers.PostProvider;
import piii.app.culturapp.providers.UserProvider;
import piii.app.culturapp.utils.RelativeTime;

public class MyPostsAdapter extends FirestoreRecyclerAdapter<Post, MyPostsAdapter.ViewHolder> {

    Context context;
    PostProvider mPostProvider;
    UserProvider mUserProvider;
    LikeProvider mLikeProvider;
    AuthProvider mAuthProvider;

    public MyPostsAdapter(FirestoreRecyclerOptions<Post> options, Context context) {
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
        String relativeTime = RelativeTime.getTimeAgo(post.getTimestamp(), context);
        holder.textViewRelativeTime.setText(relativeTime);
        holder.textViewTitle.setText(post.getTitle());

        if (post.getIdUser().equals(mAuthProvider.getUid())){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }else{
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        if (post.getImage1() != null) {
            if (!post.getImage1().isEmpty()) {
                Picasso.with(context).load(post.getImage1()).into(holder.circleImageMyPost);
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
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDelete(postId);
            }
        });
    }

    private void showConfirmDelete(final String postId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar Publicación")
                .setMessage("Estás seguro de realizar esta acción?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost(postId);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void deletePost(String postId) {
        mPostProvider.delete(postId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No se pudo eliminar el post", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewRelativeTime;
        CircleImageView circleImageMyPost;
        ImageView imageViewDelete;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitleMyPost);
            textViewRelativeTime = view.findViewById(R.id.textViewRelativeTimeMyPost);
            circleImageMyPost = view.findViewById(R.id.circleImageMyPost);
            imageViewDelete = view.findViewById(R.id.imageViewDeleteMyPost);
            viewHolder = view;
        }
    }

}
