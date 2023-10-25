package com.example.themet

// ArtworkAdapter.kt
import com.squareup.picasso.Picasso
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ArtworkAdapter(private val artworkList: List<Artwork>) :
    RecyclerView.Adapter<ArtworkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artwork = artworkList[position]
        holder.bind(artwork)
    }

    override fun getItemCount(): Int {
        return artworkList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view)
        private val artistTextView: TextView = itemView.findViewById(R.id.text_view2)
        private val imageView: ImageView = itemView.findViewById(R.id.image_view)

        fun bind(artwork: Artwork) {
            titleTextView.text = "Artwork Title: ${artwork.title}"

            artistTextView.text = "Artwork Artist: ${artwork.artist}"
            val imageUrl = artwork.primaryImage
            Picasso.get().load(imageUrl).into(imageView)
        }
    }
}
