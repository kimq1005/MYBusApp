package com.example.your_precioustime.SecondActivity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.your_precioustime.App
import com.example.your_precioustime.R
import com.example.your_precioustime.Retrofit.Retrofit_Client
import com.example.your_precioustime.Retrofit.Retrofit_InterFace
import com.example.your_precioustime.SecondActivity.DB.BusFavroiteDataBase
import com.example.your_precioustime.SecondActivity.DB.OnDeleteInterFace
import com.example.your_precioustime.SecondActivity.DB.TestFavoriteModel
import com.example.your_precioustime.Url
import com.example.your_precioustime.Util.Companion.TAG
import com.example.your_precioustime.databinding.SubwayFragmentBinding

@SuppressLint("StaticFieldLeak")
class SubwayFragment : Fragment(R.layout.subway_fragment), OnDeleteInterFace {
    private var setbinding: SubwayFragmentBinding? = null
    private val binding get() = setbinding!!

    lateinit var busFavoriteDB: BusFavroiteDataBase
    lateinit var busfavoriteEntity: List<TestFavoriteModel>

    lateinit var subwayAdapter: SubWayAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setbinding = SubwayFragmentBinding.bind(view)
        busFavoriteDB = BusFavroiteDataBase.getinstance(App.instance)!!

        getAll()


    }

    private fun getAll() {

        val getAllTask = (object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                busfavoriteEntity = busFavoriteDB.busFavoriteDAO().busFavoriteGetAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)

                Log.d(TAG, "onPostExecute: $busfavoriteEntity")
                setRecyclerView()

            }
        }).execute()
    }


    private fun setRecyclerView() = with(binding) {

        subwayAdapter = SubWayAdapter(this@SubwayFragment)

        subwayRecyclerView.apply {
            adapter = subwayAdapter
//            layoutManager= LinearLayoutManager(context)
            layoutManager = GridLayoutManager(App.instance, 2, GridLayoutManager.VERTICAL, false)
            subwayAdapter.submitList(busfavoriteEntity)

        }

    }


    private fun ondeleteList(testFavoriteModel: TestFavoriteModel) {

        val deleteTask = (object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                busFavoriteDB.busFavoriteDAO().busFavoriteDelete(testFavoriteModel)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAll()
            }

        }).execute()
    }

    override fun onDeleteFavroitelist(testFavoriteModel: TestFavoriteModel) {
        ondeleteList(testFavoriteModel)
    }


}