package com.robin8.rb.model.sortlist;

import com.google.gson.annotations.SerializedName;

/**
 * The class created to handle facebook login data of user
 *
 * created by TPlus Studio
 */
public class UserFacebookInfo {

    @SerializedName("id")
    public String id = "";
    @SerializedName("link")
    public String link = "";
    @SerializedName("address")
    public String address = "";
    @SerializedName("name")
    public String name = "";
    @SerializedName("email")
    public String email = "";
    @SerializedName("gender")
    public String gender = "";
    @SerializedName("picture")
    public Picture picture;
    @SerializedName("about")
    public String about = "";
    @SerializedName("locale")
    public String locale = "";
    @SerializedName("hometown")
    public String hometown = "";

    public class Picture {

        @SerializedName("data")
        public PictureDetail pictureDetail;
    }

    public class PictureDetail {

        @SerializedName("url")
        public String url;
    }

}
