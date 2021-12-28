package com.example.your_precioustime.ThridActivity

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.your_precioustime.App
import com.example.your_precioustime.Model.Item
import com.example.your_precioustime.Retrofit.Coroutines_InterFace
import com.example.your_precioustime.Retrofit.Retrofit_Client
import com.example.your_precioustime.Retrofit.Retrofit_InterFace
import com.example.your_precioustime.SecondActivity.Bus_Station_Search_Adapter
import com.example.your_precioustime.SecondActivity.DB.BUSDNumberDataBase
import com.example.your_precioustime.SecondActivity.DB.BUSDataBase
import com.example.your_precioustime.SecondActivity.DB.BUSEntity
import com.example.your_precioustime.SecondActivity.DB.BUSNumEntity
import com.example.your_precioustime.Url.Companion.BUS_MAIN_URL
import com.example.your_precioustime.Util.Companion.TAG
import com.example.your_precioustime.databinding.ActivityBusSubwayBinding
import kotlinx.coroutines.*
import retrofit2.create
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.math.log

@SuppressLint("StaticFieldLeak")

class BusSubwayActivity : AppCompatActivity() ,CoroutineScope {
    private var busSubwayActivityBinding : ActivityBusSubwayBinding? =null
    private val binding get() = busSubwayActivityBinding!!

    lateinit var busStationSearchAdapter: Bus_Station_Search_Adapter


    private lateinit var job:Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val coroutinesInterface : Coroutines_InterFace=Retrofit_Client.getClient(BUS_MAIN_URL)
        .create(Coroutines_InterFace::class.java)

    private val retrofitInterFace:Retrofit_InterFace = Retrofit_Client.getClient(BUS_MAIN_URL)
        .create(Retrofit_InterFace::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        busSubwayActivityBinding = ActivityBusSubwayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()


        binding.fuckingRecyclerbutton.setOnClickListener {
            fuckinyeah("31010","우남")
        }


    }

    private fun fuckinyeah(citycode:String, stationName:String){
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO){
                    val call = coroutinesInterface.Coroutines_BUS_NAMEGET(citycode,stationName)
                    val body = call.body()

                    Log.d(TAG, "fuckinyeah: $body")

                    if(call.isSuccessful){
                        val hello = body?.body?.items?.item
                        Log.d(TAG, "fuckinyeah: $hello")
//                        busStationSearchAdapter = Bus_Station_Search_Adapter()

                        binding.fuckingshit.apply {
                            adapter = busStationSearchAdapter
                            layoutManager = LinearLayoutManager(context)
                            busStationSearchAdapter.submitList(hello)
                        }
                    }

//                    body?.let{
//                        val hello = body.body.items.item
//                        busStationSearchAdapter = Bus_Station_Search_Adapter()
//
////                        binding.fuckingshit.apply {
////                            adapter = busStationSearchAdapter
////                            layoutManager = LinearLayoutManager(this@BusSubwayActivity)
////                            busStationSearchAdapter.submitList(hello)
////
////                        }
//                    }

                }

            }catch (e:Exception){
                Toast.makeText(this@BusSubwayActivity,"에러다 이러ㅕㄴ아",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun testest(citycode: String, stationName: String){
        launch(coroutineContext) {
            try {
                val call = retrofitInterFace.StationNameGet(citycode,stationName)
                val body = call.body()
            }catch (e:Exception){
                Log.d(TAG, "testest:fdsfsfsfdsf")
            }
        }

    }


}