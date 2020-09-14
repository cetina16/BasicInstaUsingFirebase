package com.betulcetin.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    FeedRecyclerAdapter feedRecyclerAdapter;

    ArrayList<String> userEmailFb;
    ArrayList<String> commentFb;
    ArrayList<String> imagesFb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        userEmailFb = new ArrayList<>();
        commentFb = new ArrayList<>();
        imagesFb = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


       feedRecyclerAdapter = new FeedRecyclerAdapter(userEmailFb,commentFb,imagesFb);

        recyclerView.setAdapter(feedRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.upload){
            Intent intentUpload = new Intent(FeedActivity.this,UploadActivity.class);
            startActivity(intentUpload);
        }else if(item.getItemId() == R.id.signout){
            firebaseAuth.signOut();

            Intent intentOut = new Intent(FeedActivity.this,SignActivity.class);
            startActivity(intentOut);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void getDataFromFirestore(){
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value != null){
                    for(DocumentSnapshot snapshot: value.getDocuments()){
                        Map<String,Object> data = snapshot.getData();

                        String comment = (String) data.get("comment");
                        String userEmail = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");

                        userEmailFb.add(userEmail);
                        commentFb.add(comment);
                        imagesFb.add(downloadUrl);

                        feedRecyclerAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }
}