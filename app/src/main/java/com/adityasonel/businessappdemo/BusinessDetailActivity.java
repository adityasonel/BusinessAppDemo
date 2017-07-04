package com.adityasonel.businessappdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by HeisenBerg on 6/29/2017.
 */

public class BusinessDetailActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    TextView nameTv, descriptionTv, ratingTv, addressTv, cityTv;
    ImageView businessImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail_screen);

        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(BusinessDetailActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("business_list");

        nameTv = (TextView) findViewById(R.id.show_business_name);
        descriptionTv = (TextView) findViewById(R.id.show_description);
        ratingTv = (TextView) findViewById(R.id.show_rating);
        addressTv = (TextView) findViewById(R.id.show_address);
        cityTv = (TextView) findViewById(R.id.show_city);
        businessImage = (ImageView) findViewById(R.id.business_image);


        Bundle extras = getIntent().getExtras();
        String NAME = (String) extras.get("Name");
        String DESCRIPTION = (String) extras.get("Description");
        float RATING = (float) extras.get("Rating");
        String ADDRESS = (String) extras.get("Address");
        String CITY = (String) extras.get("City");
        String IMAGE = (String) extras.get("ImageUrl");

        nameTv.setText(NAME);
        descriptionTv.setText(DESCRIPTION);
        ratingTv.setText(String.valueOf(RATING));
        addressTv.setText(ADDRESS);
        cityTv.setText(CITY);

        try{

            if (IMAGE.equals(""))
            {
                Picasso.with(getApplicationContext()).load(R.mipmap.ic_launcher).resize(ViewGroup.LayoutParams.FILL_PARENT, 160).into(businessImage);
            } else {

                Picasso.with(getApplicationContext()).load(IMAGE).resize(160, 160).into(businessImage);
            }
        } catch (NullPointerException ex){}
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
