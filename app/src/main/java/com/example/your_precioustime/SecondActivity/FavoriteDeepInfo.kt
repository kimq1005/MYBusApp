package com.example.your_precioustime.SecondActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.your_precioustime.Model.Bus
import com.example.your_precioustime.Model.Item
import com.example.your_precioustime.Retrofit.Coroutines_InterFace
import com.example.your_precioustime.Retrofit.Retrofit_Client
import com.example.your_precioustime.Retrofit.Retrofit_InterFace
import com.example.your_precioustime.Url
import com.example.your_precioustime.Util.Companion.TAG
import com.example.your_precioustime.databinding.ActivityFavoriteDeepInfoBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class FavoriteDeepInfo : AppCompatActivity(), CoroutineScope {

    private var favroitebinding: ActivityFavoriteDeepInfoBinding? = null
    private val binding get() = favroitebinding!!

    private val retrofitInterface: Retrofit_InterFace =
        Retrofit_Client.getClient(Url.BUS_MAIN_URL).create(Retrofit_InterFace::class.java)

    private val coroutinesInterface: Coroutines_InterFace =
        Retrofit_Client.getClient(Url.BUS_MAIN_URL).create(Coroutines_InterFace::class.java)

    lateinit var DFadapter: DeepFavoriteAdapter

    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favroitebinding = ActivityFavoriteDeepInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

//        setApiRecyclerView()
        coroutineRecyclerView()

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setApiRecyclerView() = with(binding) {
        val favoritenodenum = intent.getStringExtra("favoritenodenum").toString()
        val favoriteStationName = intent.getStringExtra("favoriteStationName").toString()
        BusStationName.text = favoriteStationName

        val call = retrofitInterface.BusGet("31010", favoritenodenum)

        call.enqueue(object : retrofit2.Callback<Bus> {
            override fun onResponse(call: Call<Bus>, response: Response<Bus>) {
                Log.d(TAG, "onResponse: ${response.body()}")
                val body = response.body()

                body?.let {
                    val wow = body.body.items.item
                    val mylist = mutableListOf<Item>()

                    for (i in wow.indices) {
                        val busNm: String
                        val waitbus: Int
                        val waittime: Int

                        busNm = wow.get(i).routeno!!
                        waitbus = wow.get(i).arrprevstationcnt!!
                        waittime = wow.get(i).arrtime!!


                        mylist.add(
                            Item(
                                busNm, waitbus, waittime
                            )
                        )

                        val firstList = mylist.filterIndexed { index, i ->
//                        Log.d(TAG, "??????????????? ?????? ???????????? : $index , $i")
                            index % 2 == 0    //?????? ?????? ???????????? ???????????? ???????????????
                        }

                        val secondList = mylist.filterIndexed { index, item ->
                            index % 2 == 1
                        }

                        val ResultList = mutableListOf<Item>()

                        firstList.forEach {
                            val ARouteNo = it.routeno
                            val AWaitstation = it.arrprevstationcnt
                            val AWaitTime = it.arrtime
                            var found = false


                            secondList.forEach {
                                val BRouteNo = it.routeno
                                val BWaitstation = it.arrprevstationcnt
                                val BWaitTime = it.arrtime

                                if (ARouteNo == BRouteNo) {
                                    if (AWaitstation!! > BWaitstation!!) {
                                        ResultList.add(
                                            Item(
                                                it.routeno,
                                                it.arrprevstationcnt,
                                                it.arrtime
                                            )
                                        )

                                    } else {
                                        ResultList.add(Item(ARouteNo, AWaitstation, AWaitTime))
                                    }

//                                    Log.d(TAG, "?????? ????????? ?????????: $ResultList")
                                    Log.d(TAG, "onResponse:?????? ????????? ?????????")
                                }


                            }

                            DFadapter = DeepFavoriteAdapter()

                            FravroitestationinfoRecyclerView.apply {
                                adapter = DFadapter
                                layoutManager = GridLayoutManager(
                                    this@FavoriteDeepInfo,
                                    2,
                                    GridLayoutManager.VERTICAL,
                                    false
                                )
                                DFadapter.submitList(ResultList)
                            }


                        }


                    }


                }
            }

            override fun onFailure(call: Call<Bus>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })
    }

    private fun coroutineRecyclerView() = with(binding) {


        launch(coroutineContext) {
            try{

                val favoritenodenum = intent.getStringExtra("favoritenodenum").toString()
                val favoriteStationName = intent.getStringExtra("favoriteStationName").toString()
                BusStationName.text = favoriteStationName

                val response = coroutinesInterface.BusGet("31010", favoritenodenum)
                if (response.isSuccessful) {
                    val body = response.body()

                    body?.let {
                        val wow = body.body.items.item
                        val mylist = mutableListOf<Item>()

                        for (i in wow.indices) {
                            val busNm: String
                            val waitbus: Int
                            val waittime: Int

                            busNm = wow.get(i).routeno!!
                            waitbus = wow.get(i).arrprevstationcnt!!
                            waittime = wow.get(i).arrtime!!


                            mylist.add(
                                Item(
                                    busNm, waitbus, waittime
                                )
                            )

                            val firstList = mylist.filterIndexed { index, i ->
//                        Log.d(TAG, "??????????????? ?????? ???????????? : $index , $i")
                                index % 2 == 0    //?????? ?????? ???????????? ???????????? ???????????????
                            }

                            val secondList = mylist.filterIndexed { index, item ->
                                index % 2 == 1
                            }

                            val ResultList = mutableListOf<Item>()

                            firstList.forEach {
                                val ARouteNo = it.routeno
                                val AWaitstation = it.arrprevstationcnt
                                val AWaitTime = it.arrtime
                                var found = false


                                secondList.forEach {
                                    val BRouteNo = it.routeno
                                    val BWaitstation = it.arrprevstationcnt
                                    val BWaitTime = it.arrtime

                                    if (ARouteNo == BRouteNo) {
                                        if (AWaitstation!! > BWaitstation!!) {
                                            ResultList.add(
                                                Item(
                                                    it.routeno,
                                                    it.arrprevstationcnt,
                                                    it.arrtime
                                                )
                                            )

                                        } else {
                                            ResultList.add(Item(ARouteNo, AWaitstation, AWaitTime))
                                        }

//                                    Log.d(TAG, "?????? ????????? ?????????: $ResultList")

                                    }


                                }

                                DFadapter = DeepFavoriteAdapter()

                                FravroitestationinfoRecyclerView.apply {
                                    adapter = DFadapter
                                    layoutManager = GridLayoutManager(
                                        this@FavoriteDeepInfo,
                                        2,
                                        GridLayoutManager.VERTICAL,
                                        false
                                    )
                                    DFadapter.submitList(ResultList)
                                }


                            }


                        }


                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@FavoriteDeepInfo, "??????", Toast.LENGTH_SHORT).show()
            }
        }

    }


}