package piii.app.culturapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import piii.app.culturapp.models.Post;

public class PostProvider {

    CollectionReference mCollection;

    public PostProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Post");
    }

    public Task<Void> save(Post post){
        return mCollection.document().set(post);
    }
}
