package org.wit.placemark.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlacemarkModel(var id: Long = 0,
                          var title: String = "",
                          var description: String = "",
                          var townland: String = "",
                          var country: String = "",
                          var date: String = "",
                          var notes: String = "",
                          var image: String = "",
                          var lat: String = "",
                          var lng: String = "" ) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable


