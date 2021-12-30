package com.example.your_precioustime.SecondActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.your_precioustime.App
import com.example.your_precioustime.Model.Item
import com.example.your_precioustime.R
import com.example.your_precioustime.ThridActivity.BusSubwayActivity
import com.example.your_precioustime.Util.Companion.TAG
import com.example.your_precioustime.databinding.BusitemLayoutBinding
import kotlinx.android.synthetic.main.busitem_layout.view.*


import android.view.View
import com.example.your_precioustime.SecondActivity.DB.*
import java.util.concurrent.TimeUnit


@SuppressLint("StaticFieldLeak")
class UpAdpater:RecyclerView.Adapter<UpAdpater.MyViewHolder>() {
    private var item: List<Item>? = null

    lateinit var busstationnameDB : BUSStationNameDataBase
    lateinit var busstationnameEntity : List<BUSStationNameEntity>


    class MyViewHolder(val binding:BusitemLayoutBinding): RecyclerView.ViewHolder(binding.root){



        fun bind(item: Item){

            val mytime = item.arrtime!!
            val second = mytime/60

            binding.BusNumber.text =item.routeno.toString()
            binding.waitBusNumber.text = item.arrprevstationcnt.toString()
            binding.waitTime.text = second.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = BusitemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        item?.get(position)?.let {
            holder.bind(it)
        }

        var checkstars:Boolean = true
        //난 재능이 없어..



//        holder.itemView.stars.setOnClickListener {
//            if (checkstars == true) {
//                holder.itemView.stars.setImageResource(R.drawable.shinigstar)
//                checkstars = false
//
//                val busnumber = item?.get(position)?.routeno
//                val bussavenumber = BUSNumEntity(
//                    null,
//                    busnumber
//                )
//                BUSNumInsert(bussavenumber)
//                //DB저장
//
//                Toast.makeText(holder.itemView.context,"즐겨찾기에 등록 되었습니다 ",Toast.LENGTH_SHORT).show()
//            } else {
//                holder.itemView.stars.setImageResource(R.drawable.star)
//                checkstars = true
//
//                //DB삭제
//
//
//                Toast.makeText(holder.itemView.context,"즐겨찾기에서 해제 되었습니다",Toast.LENGTH_SHORT).show()
//            }
//        }





    }



    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list:List<Item>){
        item = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return item?.size!!
    }




//    private fun businsert(busEntity: BUSEntity){
//
//        busDataBase = BUSDataBase.getinstance(App.instance)!!
//
//        var businsertTask = (object : AsyncTask<Unit,Unit,Unit>(){
//            override fun doInBackground(vararg params: Unit?) {
//                busDataBase.busDAO().businsert(busEntity)
//            }
//
//            override fun onPostExecute(result: Unit?) {
//                super.onPostExecute(result)
//                busGetAll()
//            }
//        }).execute()
//
//    }
//
//    private fun busGetAll(){
//        val busGetAllTask = (object:AsyncTask<Unit,Unit,Unit>(){
//            override fun doInBackground(vararg params: Unit?) {
//                busEntity=busDataBase.busDAO().busgetAll()
//            }
//        }).execute()
//    }

//    private fun BUSnumInsert(busnumEntity: BUSNumEntity){
//
//        busnumDataBase = BUSDNumberDataBase.getinstance(App.instance)!!
//
//        var businsertTask = (object : AsyncTask<Unit,Unit,Unit>(){
//            override fun doInBackground(vararg params: Unit?) {
//                busnumDataBase.busnumDAO().busnumInsert(busnumEntity)
//            }
//
//            override fun onPostExecute(result: Unit?) {
//                super.onPostExecute(result)
//                busnumGetAll()
//            }
//        }).execute()
//
//    }
//
//    private fun busnumGetAll(){
//        val busGetAllTask = (object:AsyncTask<Unit,Unit,Unit>(){
//            override fun doInBackground(vararg params: Unit?) {
//                busnumEntity= busnumDataBase.busnumDAO().busnumGetAll()
//            }
//        }).execute()
//    }


    private fun BUSnumInsert(busStationNameEntity: BUSStationNameEntity){

        busstationnameDB = BUSStationNameDataBase.getinstance(App.instance)!!


        var businsertTask = (object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                busstationnameDB.busstationnameDao().busStationNameInsert(busStationNameEntity)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                busstationnameGetAll()
            }
        }).execute()

    }

    private fun busstationnameGetAll(){
        val busGetAllTask = (object:AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg params: Unit?) {
                busstationnameEntity = busstationnameDB.busstationnameDao().busStationNameGetAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)

                Log.d(TAG, "onPostExecute: $busstationnameEntity")
            }
        }).execute()
    }





}