package com.shreyanshvit.my_blog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private Button mLoginBtn;
    private Button mNeddAcc;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private ProgressDialog mProgress;
    private SignInButton mGoogleBtn;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LoginActivity";
    private GoogleApiClient mGoogleApiClient;
    private int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.keepSynced(true);

        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);


        mLoginPasswordField = (EditText) findViewById(R.id.loginPasswordField);
        mLoginEmailField = (EditText) findViewById(R.id.loginEmailField);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mNeddAcc = (Button) findViewById(R.id.needAcc);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });





        mNeddAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
                backButtonCount = 0;
            }
        });

    }



    private void goToRegister() {

        Intent nedd_acc = new Intent(LoginActivity.this, RegisterActivity.class);
        nedd_acc.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(nedd_acc);

    }


    private void checkLogin() {

            String email = mLoginEmailField.getText().toString().trim();
            String password = mLoginPasswordField.getText().toString().trim();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                mProgress.setMessage("Checking Login...");
                mProgress.show();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            mProgress.dismiss();
                            checkUserExist();

                        } else {
                            mProgress.dismiss();
                            Toast.makeText(LoginActivity.this, "You don't have account", Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
    }

    private void checkUserExist() {
        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id)){

                    Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
                    mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainintent);

                }else {
                    Intent setupintent = new Intent(LoginActivity.this, SetupActivity.class);
                    setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupintent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            backButtonCount = 0;

        }
        else
        {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
