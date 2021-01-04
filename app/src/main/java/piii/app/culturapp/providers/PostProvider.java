package piii.app.culturapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import piii.app.culturapp.models.Post;

public class PostProvider {

    CollectionReference mCollection;

    public PostProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Post");
    }

    public Task<Void> save(Post post){
        return mCollection.document().set(post);
    }

    public Query getAll() {
        return mCollection.orderBy("title", Query.Direction.DESCENDING);
    }

    public Query getPostByUser(String id){
        return mCollection.whereEqualTo("idUser",id);
    }

    public Task<DocumentSnapshot> getPostById(String id){
        return mCollection.document(id).get();
    }

    public DocumentReference getPostRealTimeById(String id){
        return mCollection.document(id);
    }

    public Task<QuerySnapshot> getDocuments(){
        return mCollection.get();
    }
}
