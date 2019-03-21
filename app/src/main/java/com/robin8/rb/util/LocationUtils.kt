//package com.robin8.rb.util
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.Context
//import android.location.LocationManager
//import android.util.Log
//
//object LocationUtils{
//
//    @SuppressLint("MissingPermission")
//    fun getCity(activity:Activity){
//        var locationManger = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        var location = locationManger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//        Log.d("location",location.toString())
//    }
//}