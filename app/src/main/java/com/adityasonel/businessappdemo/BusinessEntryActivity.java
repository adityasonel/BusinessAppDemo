package com.adityasonel.businessappdemo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.adityasonel.businessappdemo.R.id.image;
import static com.adityasonel.businessappdemo.R.id.rating;

/**
 * Created by HeisenBerg on 6/29/2017.
 */

public class BusinessEntryActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText businessNameEt, descriptionEt, addressEt;
    private RatingBar ratingBar;
    private Spinner spinner;

    private static final int PICK_IMAGE_REQUEST = 111;
    private Uri filePath;

    Uri imageUrl;
    CircleImageView profileImage;

    String profileImageName;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference myStorageReference = firebaseStorage.getReferenceFromUrl("gs://businessappdemo.appspot.com");

    Button btnAdd;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_entry);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();

            startActivity(new Intent(this, LoginActivity.class));
        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.business_entry_layout);

        databaseReference = FirebaseDatabase.getInstance().getReference("business_list");

        profileImage = (CircleImageView) findViewById(R.id.profile_image);

        businessNameEt = (EditText) findViewById(R.id.businessName);
        descriptionEt = (EditText) findViewById(R.id.businessDescription);
        ratingBar = (RatingBar) findViewById(rating);
        addressEt = (EditText) findViewById(R.id.address);
        spinner = (Spinner) findViewById(R.id.cities);

        btnAdd = (Button) findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String businessName = businessNameEt.getText().toString().trim();

                String businessDescription = descriptionEt.getText().toString().trim();

                final float businessRating = ratingBar.getRating();

                String businessAddress = addressEt.getText().toString().trim();

                String businessCity = spinner.getSelectedItem().toString().trim();

                if (TextUtils.isEmpty(businessName)) {
                    Toast.makeText(getApplicationContext(), "Enter business name !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(businessDescription)) {
                    Toast.makeText(getApplicationContext(), "Enter description !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(businessAddress)) {
                    Toast.makeText(getApplicationContext(), "Enter address !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (businessCity.equals("None")) {
                    Toast.makeText(getApplicationContext(), "Select city !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(profileImageName)) {
                    Toast.makeText(getApplicationContext(), "Select Image first  !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String key = databaseReference.push().getKey();

                BusinessDetailModel businessDetailModel = new BusinessDetailModel(businessName, businessDescription, businessRating, businessAddress, businessCity, key, profileImageName);

                databaseReference.child(key).setValue(businessDetailModel);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BusinessEntryActivity.this);

                alertDialog.setTitle("Business details saved !");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }
                });
                alertDialog.show();
            }
        });
    }

    public void selectImage(View v) {

        showFileChooser();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            imageUrl = data.getData();

            profileImageName = imageUrl.toString();

            final BusinessDetailModel businessDetailModel = new BusinessDetailModel();
            businessDetailModel.setImageUrl(profileImageName);

            StorageReference filePath = myStorageReference.child(imageUrl.getLastPathSegment());

            filePath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    Picasso.with(BusinessEntryActivity.this).load(downloadUri).fit().centerCrop().into(profileImage);
                }
            });
        }
    }
}
