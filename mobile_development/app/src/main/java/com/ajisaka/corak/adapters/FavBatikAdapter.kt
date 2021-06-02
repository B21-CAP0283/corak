package com.ajisaka.corak.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ajisaka.corak.R
import com.ajisaka.corak.databinding.ItemFavoriteCardBinding
import com.ajisaka.corak.model.entities.FavBatik
import com.ajisaka.corak.ui.detail.DetailActivity
import com.ajisaka.corak.ui.favorite.FavoriteFragment
import com.bumptech.glide.Glide


class FavBatikAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<FavBatikAdapter.ViewHolder>() {

    private var listBatik: List<FavBatik> = listOf()

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFavoriteCardBinding =
            ItemFavoriteCardBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
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

        val batik = listBatik[position]
        Log.d("Batik list Adapter", batik.toString())
        // Load the dish image in the ImageView.

        Glide.with(fragment)
            .load(batik.image)
            .centerCrop()
            .placeholder(R.drawable.img_not_found)
            .into(holder.ivBatikImage)

        holder.tvTitle.text = batik.name
        holder.tvOrigin.text = batik.origin
        holder.btnDetail.setOnClickListener{
            val intent = Intent(fragment.context, DetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("EXTRA_NAME", batik.name)
            intent.putExtra("EXTRA_CONFIDENCE", batik.confidence)
            intent.putExtra("EXTRA_ORIGIN", batik.origin)
            intent.putExtra("EXTRA_IMAGE", batik.image)
            intent.putExtra("EXTRA_AUDIO", batik.audio)
            intent.putExtra("EXTRA_CHARACTERISTIC", batik.characteristic)
            intent.putExtra("EXTRA_PHILOSOPHY", batik.philosophy)
            fragment.context?.startActivity(intent)
        }

        holder.btnFav.setOnClickListener{
            if (fragment is FavoriteFragment) {
                fragment.deleteStudent(batik)
            }
        }
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
    class ViewHolder(view: ItemFavoriteCardBinding) : RecyclerView.ViewHolder(view.root) {
        // Holds the TextView that will add each item to
        val ivBatikImage = view.ivBatikImage
        val tvTitle = view.tvItemName
        val tvOrigin = view.tvOrigin
        val btnDetail = view.btnDetail
        val btnFav = view.ivFavoriteBatik
    }
}