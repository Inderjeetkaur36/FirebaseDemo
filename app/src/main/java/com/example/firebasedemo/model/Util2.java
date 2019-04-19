package com.example.firebasedemo.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class Util2 {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Products.db";

    public static final String TAB_NAME = "Shoes";

    public static final String COL_ID = "_ID";
    public static final String COL_SIZE = "size";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_COLOR = "color";

    public static final String CREATE_TAB_QUERY = "create table Customer(" +
            " _ID integer primary key autoincrement," +
            " size varchar(10)," +
            " name varchar(256)," +
            " price varchar(256)," +
            " color varchar(20)," +
            " )";

    public static final Uri SHOES_URI = Uri.parse("content://com.example.firebasedemo.mycp/"+TAB_NAME);

    /*
        create table Customer(
            _ID int primary key autoincrement,
            name varchar(256),
            phone varchar(256),
            email varchar(256)
        )
     */

    public static boolean isInternetConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
}
