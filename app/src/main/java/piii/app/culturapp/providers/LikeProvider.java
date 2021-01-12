package piii.app.culturapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import piii.app.culturapp.models.Like;

public class LikeProvider {
    CollectionReference mCollection;

    public LikeProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Like");
    }

    public Task<Void> create(Like like) {
        DocumentReference document = mCollection.document();
        String id = document.getId();
        like.setId(id);
        return document.set(like);
    }

    public Query getLikeByPostAndUser(String idPost, String idUser) {
        return mCollection.whereEqualTo("idPost", idPost).whereEqualTo("idUser", idUser);
    }

    public Task<Void> delete(String id) {
        return mCollection.document(id).delete();
    }

    public Query getLiekesByPost(String idPost) {
        return mCollection.whereEqualTo("idPost", idPost);
    }

}
