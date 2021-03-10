package com.example.lokasyonislemleri

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private var izinKontrol:Int = 0

    private lateinit var flpc:FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flpc = LocationServices.getFusedLocationProviderClient(this) //instance yarattık

        buttonKonumAl.setOnClickListener{
            izinKontrol = ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
            //android manifest dosyası izni almış mı diye bakmak için ona eriştik
            if(izinKontrol != PackageManager.PERMISSION_GRANTED){
                //izin onayı verilmemişse burası çalışır
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            }else{
                //daha önce izne onay verilmişse burası çalışır
                locationTask = flpc.lastLocation
                konumBilgisiAl()
            }
        }
    }
    fun konumBilgisiAl(){
        locationTask.addOnSuccessListener {location: Location ->
            if(location != null){
                //konumun içerisinde bilgi varsa
                textViewEnlem.text = "Enlem: ${location.latitude}"
                textViewBoylam.text = "Boylam: ${location.longitude}"
            }else{
                textViewEnlem.text = "Konum Alınamadı"
                textViewBoylam.text = "Konum Alınamadı"
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 100){

            izinKontrol = ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "İzin kabul edildi", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext, "İzin reddedildi", Toast.LENGTH_LONG).show()
            }
        }
    }

}