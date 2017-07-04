package com.adityasonel.businessappdemo;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by HeisenBerg on 6/30/2017.
 */

public class BusinessDetailModel{

    public String businessName, description, address, city, imageUrl, key;
    public float ratingValue;

    public BusinessDetailModel(){

    }

    public BusinessDetailModel(String businessName, String description, float ratingValue, String address, String city, String key, String imageUrl){

        this.businessName = businessName;
        this.description = description;
        this.ratingValue = ratingValue;
        this.address = address;
        this.city = city;
        this.key = key;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
