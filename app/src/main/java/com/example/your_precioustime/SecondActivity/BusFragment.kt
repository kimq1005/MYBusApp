package com.example.your_precioustime.SecondActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.your_precioustime.App
import com.example.your_precioustime.Model.*
import com.example.your_precioustime.R
import com.example.your_precioustime.Retrofit.Coroutines_InterFace
import com.example.your_precioustime.Retrofit.Retrofit_Client
import com.example.your_precioustime.Retrofit.Retrofit_InterFace
import com.example.your_precioustime.Retrofit.Retrofit_Manager
import com.example.your_precioustime.ThridActivity.BusSubwayActivity
import com.example.your_precioustime.Url
import com.example.your_precioustime.Util.Companion.TAG
import com.example.your_precioustime.databinding.BusFragmentBinding
import kotlinx.android.synthetic.main.subway_fragment.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import kotlin.Exception
import kotlin.coroutines.CoroutineContext


class BusFragment : Fragment(R.layout.bus_fragment), CoroutineScope {

    lateinit var upAdpater: UpAdpater
    private var busbinding: BusFragmentBinding? = null
    private val binding get() = busbinding!!
    private var hicityCode: String? = null
    lateinit var busStationSearchAdapter: Bus_Station_Search_Adapter


    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private val coroutinesInterface: Coroutines_InterFace =
        Retrofit_Client.getClient(Url.BUS_MAIN_URL)
            .create(Coroutines_InterFace::class.java)


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        busbinding = BusFragmentBinding.bind(view)
        job = Job()


        ClickSearchBtn()

    }


    private fun ClickSearchBtn() = with(binding) {

        clickhere.setOnClickListener {
            val suwoncitycode: String = "31010"
            val StationEditName = SearchEditText.text.toString()
            SetRecyclerView(suwoncitycode, StationEditName)
        }

        SearchEditText.setOnClickListener {
            noResultTextView.visibility = View.INVISIBLE
        }

    }

    private fun setAdapter(item: List<StationItem>) = with(binding) {

        busStationSearchAdapter = Bus_Station_Search_Adapter()
        binding.busRecyclerView.apply {
            adapter = busStationSearchAdapter
            layoutManager = LinearLayoutManager(context)
            busStationSearchAdapter.submitList(item)
        }

    }

    private fun SetRecyclerView(citycode: String, stationName: String?) {

        launch(coroutineContext) {
            try {
                val call = coroutinesInterface.Coroutines_BUS_NAMEGET(citycode, stationName)
                val body = call.body()
                Log.d(TAG, "SetRecyclerView: $body")


                if (call.isSuccessful) {
                    body?.let {
                        val hello = body.body.items.item
                        setAdapter(hello)

                        //하 인생
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT).show()
            }
        }

//        val stationcalls= retrofitInterface.StationNameGet(
//            cityCode = citycode,
//            staionName = stationName
//        )

//        stationcalls.enqueue(object :retrofit2.Callback<StationBus>{
//
//            override fun onResponse(call: Call<StationBus>, response: Response<StationBus>) {
//                val body = response.body()
//                busStationSearchAdapter = Bus_Station_Search_Adapter()
//
//                body?.let{it->
//                    val hello = body.body.items.item
//                    busRecyclerView.apply {
//                        adapter = busStationSearchAdapter
//                        layoutManager = LinearLayoutManager(context)
//                        busStationSearchAdapter.submitList(hello)
//                    }
//                }
//           }
//            override fun onFailure(call: Call<StationBus>, t: Throwable) {
//                Log.d(TAG, "onFailure:$t")
//                noResultTextView.visibility = View.VISIBLE
//
//            }
//
//        })


//        val buscall = retrofitInterface.BusGet(citycode,nodeId)
//
//        buscall.enqueue(object:retrofit2.Callback<Bus>{
//            override fun onResponse(call: Call<Bus>, response: Response<Bus>) {
//                val body = response.body()
//                val higg = body?.body?.items
//                Log.d(TAG, "onResponse: ${higg}")
//                body?.let{Bus->
//
//                    busRecyclerView.apply {
//                        val hello = Bus.body.items.item
//                        adapter=upAdpater
//                        upAdpater.submitList(hello)
//                        layoutManager = LinearLayoutManager(context)
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<Bus>, t: Throwable) {
//                Log.d(TAG, "onFailure: $t")
//            }
//
//        })

    }


    private fun CoroutinesCall(citycode: String, stationName: String) = with(binding) {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = Retrofit_Client.Retrofit_Object.Coroutines_BUS_NAMEGET(
                        citycode,
                        stationName
                    )

                    if (response.isSuccessful) {
                        val body = response.body()
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "CoroutinesCall:${response.body()}")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "CoroutinesCall:에러다에러다에러")
            }
        }
    }
}


