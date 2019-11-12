package org.wit.placemark.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.placemark.R
import org.wit.placemark.helpers.readImage
import org.wit.placemark.helpers.readImageFromPath
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.Location
import org.wit.placemark.models.PlacemarkModel
import java.util.*

class PlacemarkActivity : AppCompatActivity(), AnkoLogger,View.OnClickListener  {


  var placemark = PlacemarkModel()
  lateinit var app: MainApp
  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2
  var location = Location(52.245696, -7.139102, 15f)
  private var pickerDialog: DatePickerDialog? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark)
    toolbarAdd.title = title
    setSupportActionBar(toolbarAdd)
    info("Placemark Activity started..")

    app = application as MainApp
    var edit = false

    if (intent.hasExtra("placemark_edit")) {
      edit = true
      placemark = intent.extras?.getParcelable<PlacemarkModel>("placemark_edit")!!
      placemarkTitle.setText(placemark.title)
      description.setText(placemark.description)
      placemarkTownland.setText(placemark.townland)
      placemarkCountry.setText(placemark.country)
      placemarkNotes.setText(placemark.notes)
      placemarkDate.setText(placemark.date)
      placemarkLatitude.setText(placemark.lat)
      placemarkLongitude.setText(placemark.lng)
      placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))
      if (placemark.image != null) {
        chooseImage.setText(R.string.change_placemark_image)
      }
      btnAdd.setText(R.string.save_placemark)
    }

    placemarkDate.setOnClickListener(this)

    btnAdd.setOnClickListener() {
      placemark.title = placemarkTitle.text.toString()
      placemark.description = description.text.toString()
      placemark.townland = placemarkTownland.text.toString()
      placemark.country = placemarkCountry.text.toString()
      placemark.date = placemarkDate.text.toString()
      placemark.notes = placemarkNotes.text.toString()
      placemark.lat = placemarkLatitude.text.toString()
      placemark.lng = placemarkLongitude.text.toString()

      if (placemark.title.isEmpty()) {
        toast(R.string.enter_placemark_title)
      } else {
        if (edit) {
          app.placemarks.update(placemark.copy())
          Toast.makeText(this, "List updated", Toast.LENGTH_SHORT).show()
        } else {
          app.placemarks.create(placemark.copy())
          Toast.makeText(this, "List Created", Toast.LENGTH_SHORT).show()
        }
      }
      info("add Button Pressed: $placemarkTitle")
      setResult(AppCompatActivity.RESULT_OK)
      finish()
    }

    chooseImage.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST)
    }

    placemarkLocation.setOnClickListener {
      location.lat = java.lang.Double.parseDouble(placemarkLatitude.text.toString())
      location.lng = java.lang.Double.parseDouble(placemarkLongitude.text.toString())
      startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
    }
  }

  override fun onClick(p0: View?) {
    if (p0 == placemarkDate){
      getDate()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_placemark, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_cancel -> {
        finish()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {
      IMAGE_REQUEST -> {
        if (data != null) {
          placemark.image = data.getData().toString()
          placemarkImage.setImageBitmap(readImage(this, resultCode, data))
          chooseImage.setText(R.string.change_placemark_image)
          placemarkLocation.setText(R.string.change_placemark_location)
        }
      }
      LOCATION_REQUEST -> {
        if (data != null) {
          location = data.extras?.getParcelable<Location>("location")!!
        }
      }
    }
  }

  private fun getDate() {
    val cldr = Calendar.getInstance()
    val day = cldr.get(Calendar.DAY_OF_MONTH)
    val month = cldr.get(Calendar.MONTH)
    val year = cldr.get(Calendar.YEAR)
    pickerDialog = DatePickerDialog(this, R.style.DialogTheme,
      DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
        placemarkDate.setText(day.toString() + "/" + (month + 1) + "/" + year)

      }, year, month, day
    )
    pickerDialog!!.show()
  }
}

