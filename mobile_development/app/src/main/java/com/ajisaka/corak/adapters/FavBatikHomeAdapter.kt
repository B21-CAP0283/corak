package com.ajisaka.corak.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ajisaka.corak.R
import com.ajisaka.corak.databinding.ItemHomeHorizontalBinding
import com.ajisaka.corak.model.entities.FavBatik
import com.bumptech.glide.Glide


class FavBatikHomeAdapter(private val fragment: Fragment,private val itemAdapterCallback : ItemAdapterCallback,) :
    RecyclerView.Adapter<FavBatikHomeAdapter.ViewHolder>() {

    private var listBatik: List<FavBatik> = listOf()

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_home_horizontal, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(listBatik[position], itemAdapterCallback)
        // Load the dish image in the ImageView.
    }
    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return listBatik.size
    }

    fun batikList(list: List<FavBatik>) {
        listBatik = list
        notifyDataSetChanged()
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        // Holds the TextView that will add each item to
        private val binding = ItemHomeHorizontalBinding.bind(itemView)
        fun bind(data : FavBatik, itemAdapterCallback: ItemAdapterCallback) {
            itemView.apply {
                Glide.with(itemView)
                    .load(data.image)
                    .centerCrop()
                    .placeholder(R.drawable.img_not_found)
                    .into(binding.ivPoster)

                binding.tvBatikName.text = data.name
                itemView.setOnClickListener {
                    itemAdapterCallback.onClick(it, data)
                }
            }

        }
    }
    interface ItemAdapterCallback {
        fun onClick(v: View, data:FavBatik)
    }
}