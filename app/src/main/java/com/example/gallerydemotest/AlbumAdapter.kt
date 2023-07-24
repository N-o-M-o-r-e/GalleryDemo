package com.example.gallerydemotest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import java.io.File

class AlbumAdapter(private val glide: RequestBuilder<Bitmap> , val context: Context) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var items = mapOf<File, List<File>>()
    private var indexes = listOf<File>()


    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) = holder.bind(getItemAt(position), glide,context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder =
        AlbumViewHolder.new(parent)


    override fun getItemCount(): Int = indexes.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(fileMap: Map<File, List<File>>) {
        items = fileMap
        indexes = fileMap.keys.toList().sortedBy { it.nameWithoutExtension }
        notifyDataSetChanged()
    }

    private fun getItemAt(position: Int) = items[indexes[position]]


    class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val image: ImageView = view.findViewById(R.id.album_image)
        private val title: TextView = view.findViewById(R.id.album_title)
        private val counter: TextView = view.findViewById(R.id.album_counter)

        companion object {
            fun new(viewGroup: ViewGroup) = AlbumViewHolder(
                LayoutInflater.from(viewGroup.context).inflate(R.layout.album, viewGroup, false)
            )
        }

        fun bind(files: List<File>?, glide: RequestBuilder<Bitmap>, context: Context) {
            if (files != null) {
                (files.isNotEmpty()).let {
                    val firstChild = files[0]
                    val parent = File(firstChild.parent as String)

                    title.text = parent.nameWithoutExtension
                    glide.load(firstChild).centerCrop().into(image)
                    Glide.with(context).load(firstChild).into(image)
                    var items = 0
                    try {
                        items = getImageVideoNumber(parent)
                    } catch (e: Exception) {
                        Log.d("GETIMAGEVIDEONUMBER", e.message.toString())
                    }

                    counter.text = if (items > 1) counter.context.getString(
                        R.string.multi_items, items.toString()
                    ) else counter.context.getString(R.string.single_item)

//                image.setOnClickListener {
//                    val gson = Gson()
//                    val album = Album(parent.absolutePath, parent.nameWithoutExtension, items)
//                    val albumData = gson.toJson(album)
//                    val bundle = bundleOf("albumData" to albumData)
//                    it.findNavController()
//                        .navigate(R.id.action_SecondFragment_to_ViewAlbumFragment, bundle)
//                }
                }
            }
        }
    }
}