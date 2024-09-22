package com.example.sendlocationapp.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sendlocationapp.data.UserLocation
import com.example.sendlocationapp.databinding.ItemUserLocationBinding

class UserLocationAdapter(
    private var list: List<UserLocation>
): RecyclerView.Adapter<UserLocationAdapter.UserLocationHandler>() {

    class UserLocationHandler(
        private val binding: ItemUserLocationBinding
    ): ViewHolder(binding.root) {


        fun onBind(userLocation: UserLocation) {
            binding.run {
                latitude.text = "Широта: ${userLocation.latitude}"
                longitude.text = "Долгота: ${userLocation.longitude}"
                date.text = "Дата: ${userLocation.date}"
                time.text = "Время: ${userLocation.time}"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserLocationHandler = UserLocationHandler(
        binding = ItemUserLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: UserLocationHandler, position: Int) = holder.onBind(list[position])

    override fun getItemCount(): Int = list.size

    fun updateList(list: List<UserLocation>) {
        this.list = list
        notifyDataSetChanged()
    }

}