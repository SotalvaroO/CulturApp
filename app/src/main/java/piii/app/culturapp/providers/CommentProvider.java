package piii.app.culturapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

import piii.app.culturapp.models.Comment;

public class CommentProvider {
    CollectionReference mCollection;

    public CommentProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Comments");
    }

    public Task<Void> create(Comment comment){
        return mCollection.document().set(comment);
    }

    public Query getCommentsByPost(String idPost){
        return mCollection.whereEqualTo("idPost",idPost);
    }

}
