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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class FogetPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String Resetmail;
    private EditText mResetEmailField;
    private Button mResetBtn;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_password);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);

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
        mProgress.setMessage("Sending the mail...");
        mProgress.show();
        Resetmail = mResetEmailField.getText().toString();
        if(!TextUtils.isEmpty(Resetmail)) {
            if (mAuth != null) {
                mAuth.sendPasswordResetEmail(Resetmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mProgress.dismiss();
                        Intent mainintent = new Intent(FogetPassword.this, LoginActivity.class);
                        mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainintent);
                        Toast.makeText(FogetPassword.this, "Check your inbox", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(FogetPassword.this, "mail id not registered", Toast.LENGTH_LONG).show();
            }
        }
        else {
            mProgress.dismiss();
            Toast.makeText(FogetPassword.this, "mail id required", Toast.LENGTH_LONG).show();
        }
    }
}
