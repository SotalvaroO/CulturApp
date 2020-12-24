package piii.app.culturapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import piii.app.culturapp.models.User;

public class UserProvider {

    private CollectionReference mCollection;

    public  UserProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }
    public Task<DocumentSnapshot> getUsers(String id){
        return mCollection.document(id).get();
    }
    public DocumentReference getRealTimeUsers(String id){
        return mCollection.document(id);
    }
    public Task<Void> createUser(User user){
        return mCollection.document(user.getId()).set(user);
    }
    public Task<Void> updateUser(User user){
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        map.put("phone",user.getPhone());
        map.put("timestamp", new Date().getTime());
        map.put("image_profile",user.getImageProfile());
        map.put("image_cover",user.getImageCover());
        return mCollection.document(user.getId()).update(map);
    }
}
