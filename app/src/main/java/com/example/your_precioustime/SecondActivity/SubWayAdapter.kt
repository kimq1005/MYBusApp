package com.example.your_precioustime.SecondActivity

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_precioustime.App
import com.example.your_precioustime.SecondActivity.DB.OnDeleteInterFace
import com.example.your_precioustime.SecondActivity.DB.TestFavoriteModel

import com.example.your_precioustime.databinding.FavoritelistItemBinding


class SubWayAdapter(var onDeleteInterFace: OnDeleteInterFace) :
    ListAdapter<TestFavoriteModel, SubWayAdapter.SubwayViewHolder>(diffUtil) {
    lateinit var testFavoriteModel: List<TestFavoriteModel>

    class SubwayViewHolder(val binding: FavoritelistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(testFavoriteModel: TestFavoriteModel) {
            binding.FavoriteNameTextView.text = testFavoriteModel.stationName
            binding.FavoriteNodeIDTextView.text = testFavoriteModel.stationnodenode

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubwayViewHolder {
        val view =
            FavoritelistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubwayViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubwayViewHolder, position: Int) {
        val favoriteModelList = currentList[position]

        holder.bind(currentList[position])
        val favoriteStationName = favoriteModelList.stationName
        val favoritenodenum = favoriteModelList.stationNodeNumber

        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, FavoriteDeepInfo::class.java).apply {
                putExtra("favoriteStationName", favoriteStationName)
                putExtra("favoritenodenum", favoritenodenum)
            }


            holder.itemView.context.startActivity(intent)


        }

        holder.binding.stardeletebtn.setOnClickListener {
            onDeleteInterFace.onDeleteFavroitelist(favoriteModelList)
            Toast.makeText(holder.itemView.context, "즐겨찾기에서 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<TestFavoriteModel>() {
            override fun areItemsTheSame(
                oldItem: TestFavoriteModel,
                newItem: TestFavoriteModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: TestFavoriteModel,
                newItem: TestFavoriteModel
            ): Boolean {
                return oldItem == newItem

            }

        }

    }


}