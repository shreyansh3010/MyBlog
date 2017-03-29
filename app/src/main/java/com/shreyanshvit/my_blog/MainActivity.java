package com.shreyanshvit.my_blog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private ProgressDialog mprogress;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseUser.keepSynced(true);
        mDatabase.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent Loginintent = new Intent(MainActivity.this, LoginActivity.class);
                    Loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(Loginintent);

                }
            }
        };

        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
        mprogress = new ProgressDialog(this);

        checkUserExist();


    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        mprogress.setMessage("Please wait...");
        mprogress.show();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext() ,model.getImage());
                mprogress.dismiss();
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }


    private void checkUserExist() {

        if(mAuth.getCurrentUser() != null) {

            final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(user_id)) {

                        Intent setupintent = new Intent(MainActivity.this, SetupActivity.class);
                        setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupintent);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder {



        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title){

            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_text);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image){
            final ProgressBar mprogressBar;
            mprogressBar = (ProgressBar) mView.findViewById(R.id.progressBar5);
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image, new Callback() {
                @Override
                public void onSuccess() {
                    mprogressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add){
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }

        if(item.getItemId() == R.id.action_logout){
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }
}
