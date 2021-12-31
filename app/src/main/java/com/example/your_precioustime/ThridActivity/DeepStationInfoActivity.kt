package com.example.your_precioustime.ThridActivity

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.your_precioustime.App
import com.example.your_precioustime.Model.Bus
import com.example.your_precioustime.Model.Item
import com.example.your_precioustime.R
import com.example.your_precioustime.Retrofit.Coroutines_InterFace
import com.example.your_precioustime.Retrofit.Retrofit_Client
import com.example.your_precioustime.Retrofit.Retrofit_InterFace
import com.example.your_precioustime.SecondActivity.DB.*
import com.example.your_precioustime.SecondActivity.UpAdpater
import com.example.your_precioustime.Url
import com.example.your_precioustime.Util.Companion.TAG
import com.example.your_precioustime.databinding.ActivityDeepStationInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

@SuppressLint("StaticFieldLeak")
class DeepStationInfoActivity : AppCompatActivity() , CoroutineScope {

    private var deepStationbinding:ActivityDeepStationInfoBinding? =null
    private val binding get() = deepStationbinding!!

    private lateinit var upAdpater:UpAdpater

    lateinit var busFavoriteDB : BusFavroiteDataBase
    lateinit var activitybusfavoriteEntity: List<TestFavoriteModel>

    private val retrofitInterface: Retrofit_InterFace = Retrofit_Client.getClient(Url.BUS_MAIN_URL).create(Retrofit_InterFace::class.java)

    private val coroutinesInterface: Coroutines_InterFace = Retrofit_Client.getClient(Url.BUS_MAIN_URL)
        .create(Coroutines_InterFace::class.java)

    lateinit var job: Job

    //Coroutine
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deepStationbinding = ActivityDeepStationInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        busFavoriteDB = BusFavroiteDataBase.getinstance(App.instance)!!
        job=Job()
        val stationName = intent.getStringExtra("stationName").toString()
        binding.BusStationName.text = stationName
        binding.backbtn.setOnClickListener {
            onBackPressed()
        }

        CoroutinesSetRecyclerView()
        busFavoriteGetAll()
//        SetBusStationRecyclerView()
        savemystation()
    }

    private fun savemystation()=with(binding){

        countingstars.setOnClickListener {

            val stationName = intent.getStringExtra("stationName").toString()
            val stationNodeNumber = intent.getStringExtra("stationNodeNumber").toString()
            val stationNodeNode = intent.getStringExtra("stationnodenode").toString()

            val hello = TestFavoriteModel(
                id = null,
                checkBoolean =null,
                stationnodenode=stationNodeNode,
                stationName = stationName,
                stationNodeNumber = stationNodeNumber
            )
            BUSFravoriteInsert(hello)
        }
    }




    private fun SetBusStationRecyclerView()=with(binding) {
        val stationNodeNumber = intent.getStringExtra("stationNodeNumber").toString()

        val citycode:String = "31010"

        val call = retrofitInterface.BusGet(citycode,stationNodeNumber)
        call.enqueue(object:retrofit2.Callback<Bus>{
            override fun onResponse(call: Call<Bus>, response: Response<Bus>) {
                Log.d(TAG, "onResponse: ${response.body()}")
                upAdpater = UpAdpater()

                val body = response.body()

                body?.let{
                    val hello =body.body.items.item

                    val hi = mutableListOf<Item>()

                    for(i in hello.indices){
                        val busNm:String
                        val waitbus:Int
                        val waittime:Int

                        busNm = hello.get(i).routeno!!
                        waitbus = hello.get(i).arrprevstationcnt!!
                        waittime = hello.get(i).arrtime!!

                        hi.add(Item(
                            busNm,waitbus,waittime
                        ))

                    }
                    Log.d(TAG, "\n 전체값 리스트 : $hi \n")


                    val firstList= hi.filterIndexed { index, i ->

                        index % 2 == 0
                    }

//                    Log.d(TAG, "firstList: $firstList")

                    val secondList = hi.filterIndexed{index, item ->
                        index % 2 == 1
                    }

//                    Log.d(TAG, "secondList: $secondList")


                    val ResultList = mutableListOf<Item>()

                    firstList.forEach {
                        val ARouteNo = it.routeno
                        val AWaitstation = it.arrprevstationcnt
                        val AWaitTime = it.arrtime




                        secondList.forEach {
                            val BRouteNo = it.routeno
                            val BWaitstation = it.arrprevstationcnt

                            if(ARouteNo==BRouteNo){
                                if(AWaitstation!! > BWaitstation!!){
                                    ResultList.add(Item(it.routeno,it.arrprevstationcnt,it.arrtime))

                                }else{
                                    ResultList.add(Item(ARouteNo,AWaitstation,AWaitTime))
                                }

                            }


                        }

                        deepstationinfoRecyclerView.apply {
                            adapter = upAdpater
                            layoutManager = LinearLayoutManager(context)
                            upAdpater.submitList(ResultList)
                        }


                    }

                }

            }

            override fun onFailure(call: Call<Bus>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })




    }

    private fun CoroutinesSetRecyclerView() =with(binding){
        launch(coroutineContext) {
            try{

                val stationNodeNumber = intent.getStringExtra("stationNodeNumber").toString()
                val citycode:String = "31010"

                val response = coroutinesInterface.BusGet(citycode, stationNodeNumber)

                if(response.isSuccessful){
                    val body = response.body()
                    Log.d(TAG, "CoroutinesSetRecyclerView: $body")

                    body?.let{
                        val hello =body.body.items.item

                        val hi = mutableListOf<Item>()
                        for(i in hello.indices){
                            val busNm:String
                            val waitbus:Int
                            val waittime:Int

                            busNm = hello.get(i).routeno!!
                            waitbus = hello.get(i).arrprevstationcnt!!
                            waittime = hello.get(i).arrtime!!

                            hi.add(Item(
                                busNm,waitbus,waittime
                            ))

                        }
                        Log.d(TAG, "\n 전체값 리스트 : $hi \n")


                        val firstList= hi.filterIndexed { index, i ->

                            index % 2 == 0
                        }

//                    Log.d(TAG, "firstList: $firstList")

                        val secondList = hi.filterIndexed{index, item ->
                            index % 2 == 1
                        }

//                    Log.d(TAG, "secondList: $secondList")


                        val ResultList = mutableListOf<Item>()

                        firstList.forEach {
                            val ARouteNo = it.routeno
                            val AWaitstation = it.arrprevstationcnt
                            val AWaitTime = it.arrtime




                            secondList.forEach {
                                val BRouteNo = it.routeno
                                val BWaitstation = it.arrprevstationcnt

                                if(ARouteNo==BRouteNo){
                                    if(AWaitstation!! > BWaitstation!!){
                                        ResultList.add(Item(it.routeno,it.arrprevstationcnt,it.arrtime))

                                    }else{
                                        ResultList.add(Item(ARouteNo,AWaitstation,AWaitTime))
                                    }

                                }

                                Log.d(TAG, "띄워야할 결과: $ResultList")
                            }

                            upAdpater = UpAdpater()
                            deepstationinfoRecyclerView.apply {
                                adapter = upAdpater
                                layoutManager = LinearLayoutManager(context)
                                upAdpater.submitList(ResultList)
                            }


                        }

                    }
                }


            }catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(this@DeepStationInfoActivity,"에러",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun BUSFravoriteInsert(busfavoriteEntity:TestFavoriteModel){


        var businsertTask = (object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg params: Unit?) {

                activitybusfavoriteEntity = busFavoriteDB.busFavoriteDAO().busFavoriteGetAll()

                val stationnameList = mutableListOf<String>()

                for(i in activitybusfavoriteEntity.indices){
                    val stationname = activitybusfavoriteEntity.get(i).stationName
                    stationnameList.add(stationname)
                }

                if(binding.BusStationName.text !in stationnameList){
                    busFavoriteDB.busFavoriteDAO().busFavoriteInsert(busfavoriteEntity)
                }


            }



            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                val stationnameList = mutableListOf<String>()

                for(i in activitybusfavoriteEntity.indices){
                    val stationname = activitybusfavoriteEntity.get(i).stationName
                    stationnameList.add(stationname)
                }

                if(binding.BusStationName.text in stationnameList){
                    Toast.makeText(this@DeepStationInfoActivity,"이미 즐겨찾기에 추가된 정류장입니다!",Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this@DeepStationInfoActivity,"즐겨찾기에 추가 되었습니다!",Toast.LENGTH_SHORT).show()
                    binding.countingstars.setImageResource(R.drawable.shinigstar)
                }

            }
        }).execute()
    }

    private fun busFavoriteGetAll(){
        val busGetAllTask = (object: AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                activitybusfavoriteEntity = busFavoriteDB.busFavoriteDAO().busFavoriteGetAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                val stationnameList = mutableListOf<String>()

                for(i in activitybusfavoriteEntity.indices){
                    val stationname = activitybusfavoriteEntity.get(i).stationName
                    stationnameList.add(stationname)
                }

                if(binding.BusStationName.text in stationnameList){
                    binding.countingstars.setImageResource(R.drawable.shinigstar)
                }else{
                    binding.countingstars.setImageResource(R.drawable.star)
                }

            }

        }).execute()
    }




}