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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import piii.app.culturapp.R;
import piii.app.culturapp.models.User;
import piii.app.culturapp.providers.AuthProvider;
import piii.app.culturapp.providers.UserProvider;

public class SignupActivity extends AppCompatActivity {

    CircularImageView circularReturnButton;
    Button signupButton;
    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextRePassword;
    AuthProvider mAuth;
    UserProvider mUser;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        circularReturnButton = findViewById(R.id.circularReturnButton);
        mDialog = new SpotsDialog.Builder().setContext(this).setMessage("Espera un momento").build();
        signupButton = findViewById(R.id.buttonSignup);
        editTextUsername = findViewById(R.id.usernameSignupInput);
        editTextEmail = findViewById(R.id.emailSignupInput);
        editTextPassword = findViewById(R.id.passwordSignupInput);
        editTextRePassword = findViewById(R.id.rePasswordSignupInput);
        mAuth = new AuthProvider();
        mUser = new UserProvider();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        circularReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void signup() {
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String rePassword = editTextRePassword.getText().toString();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !rePassword.isEmpty()) {
            if (isEmailValid(email)) {
                if (password.equals(rePassword)) {
                    if (password.length() >= 8) {
                        createUser(username, email, password);
                    } else {
                        Toast.makeText(this, "La contraseña debe ser almenos de 8 caracteres", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "No coinsiden las contraseñas", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "has insertado todos los campos, email incorrecto", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Para continual ingresa todos los campos", Toast.LENGTH_SHORT).show();
        }

        /*Log.d("Campo", "username:" + username);
        Log.d("Campo", "email:" + email);
        Log.d("Campo", "password:" + password);
        Log.d("Campo", "rePassword:" + rePassword);*/
    }

    private void createUser(final String username, final String email, final String password) {
        mDialog.show();
        mAuth.signUp(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = mAuth.getUid();
                    User user = new User();
                    user.setId(userId);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);
                    mUser.createUser(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent goToMain = new Intent(SignupActivity.this, MainActivity.class);
                                goToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(goToMain);
                            } else {
                                Toast.makeText(SignupActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    mDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}