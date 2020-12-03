package piii.app.culturapp.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthProvider {
    private FirebaseAuth mAuth;

    public AuthProvider() {
        mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> signUp(String email, String password){
        return mAuth.createUserWithEmailAndPassword(email,password);
    }

    public Task<AuthResult> login(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> googleLogin(String tokenId) {
        AuthCredential credential = GoogleAuthProvider.getCredential(tokenId, null);
        return mAuth.signInWithCredential(credential);
    }

    public String getUid() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    public String getEmail() {
        if (mAuth.getCurrentUser() != null) {
            return  mAuth.getCurrentUser().getEmail();
        }else {
            return  null;
        }
    }

    public void logout(){
        if (mAuth!=null){
            mAuth.signOut();
        }
    }
    public FirebaseUser isLogged(){
        return mAuth.getCurrentUser();
    }

}
