package piii.app.culturapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import piii.app.culturapp.models.Chat;

public class ChatProvider {

    CollectionReference mCollection;

    public ChatProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Chats");
    }

    public void create(Chat chat){
        mCollection.document(chat.getIdUser1()).collection("Users").document(chat.getIdUser2()).set(chat);
        mCollection.document(chat.getIdUser2()).collection("Users").document(chat.getIdUser1()).set(chat);
    }

}
