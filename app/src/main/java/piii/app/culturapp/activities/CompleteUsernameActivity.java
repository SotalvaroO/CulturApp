package piii.app.culturapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import piii.app.culturapp.R;
import piii.app.culturapp.models.User;
import piii.app.culturapp.providers.AuthProvider;
import piii.app.culturapp.providers.UserProvider;

public class CompleteUsernameActivity extends AppCompatActivity {

    EditText usernameInput;
    Button confirmUserButton;
    AuthProvider mAuth;
    UserProvider mUser;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_username);

        usernameInput = findViewById(R.id.usernameConfirmInput);
        confirmUserButton = findViewById(R.id.buttonConfirmUser);
        mDialog = new SpotsDialog.Builder().setContext(this).setMessage("Espera un momento").build();

        mAuth = new AuthProvider();
        mUser = new UserProvider();

        confirmUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUsername();
            }
        });
    }

    private void confirmUsername() {
        String username = usernameInput.getText().toString();
        if (!username.isEmpty()) {
            updateUser(username);
        } else {
            Toast.makeText(this, "Para continual ingresa todos los campos", Toast.LENGTH_SHORT).show();
        }

        /*Log.d("Campo", "username:" + username);
        Log.d("Campo", "email:" + email);
        Log.d("Campo", "password:" + password);
        Log.d("Campo", "rePassword:" + rePassword);*/
    }

    private void updateUser(final String username) {
        String userId = mAuth.getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        mDialog.show();
        mUser.updateUser(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Intent goToMain = new Intent(CompleteUsernameActivity.this, HomeActivity.class);
                    startActivity(goToMain);
                } else {
                    Toast.makeText(CompleteUsernameActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}