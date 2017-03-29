package com.shreyanshvit.my_blog;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class FogetPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String Resetmail;
    private EditText mResetEmailField;
    private Button mResetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_password);

        mAuth = FirebaseAuth.getInstance();

        mResetEmailField = (EditText) findViewById(R.id.ResetMail);
        mResetBtn = (Button) findViewById(R.id.resetPasswrodBtn);



        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassResetViaEmail();

            }
        });




    }
    private void PassResetViaEmail(){
        if(mAuth != null) {
            Resetmail = mResetEmailField.getText().toString();
            mAuth.sendPasswordResetEmail(Resetmail);
        } else {
            Log.w(" error ", " bad entry ");
        }
    }
}
