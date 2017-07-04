package com.adityasonel.businessappdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HeisenBerg on 7/1/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.UserViewHolder>{

    private List<BusinessDetailModel> list;

    private Intent intent;

    private Context context;

    public RecyclerViewAdapter(List<BusinessDetailModel> list) {
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_screen, parent, false));
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {

        final BusinessDetailModel businessDetailModel = list.get(position);

        try {

            if (businessDetailModel.getImageUrl().equals("")){

                Picasso.with(context).load(R.mipmap.ic_launcher).resize(80, 80).into(holder.thumbnail);
            } else {

                Picasso.with(context).load(businessDetailModel.getImageUrl()).resize(80, 80).into(holder.thumbnail);
            }
        } catch (NullPointerException ex) {}


        holder.businessName.setText(businessDetailModel.businessName);
        holder.businessCity.setText(businessDetailModel.city);
        holder.businessRating.setText(businessDetailModel.ratingValue + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Context context = v.getContext();

                intent = new Intent(context, BusinessDetailActivity.class);

                intent.putExtra("Name",businessDetailModel.businessName);
                intent.putExtra("Description",businessDetailModel.description);
                intent.putExtra("Rating",businessDetailModel.ratingValue);
                intent.putExtra("Address",businessDetailModel.address);
                intent.putExtra("City",businessDetailModel.city);
                intent.putExtra("ImageUrl",businessDetailModel.imageUrl);

                context.startActivity(intent);
            }

        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        TextView businessName, businessCity, businessRating;
        CircleImageView thumbnail;

        public UserViewHolder(final View itemView) {

            super(itemView);

            thumbnail = (CircleImageView) itemView.findViewById(R.id.thumbnail);
            businessName = (TextView) itemView.findViewById(R.id.list_business_name);
            businessCity = (TextView) itemView.findViewById(R.id.list_city);
            businessRating = (TextView) itemView.findViewById(R.id.list_rating);
        }
    }
}
