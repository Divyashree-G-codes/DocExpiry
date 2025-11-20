package com.example.docexpiry

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.docexpiry.data.Card
import com.example.docexpiry.databinding.ItemCardBinding
import java.text.SimpleDateFormat
import java.util.*

// RecyclerView adapter with filter and action callbacks
class CardAdapter(
    private val onAction: (Card, Action) -> Unit
) : ListAdapter<Card, CardAdapter.VH>(DIFF), Filterable {

    private var fullList = listOf<Card>()

    enum class Action { VIEW, EDIT, DELETE, SHARE, VIEW_IMAGE }

    // Expose current full list for external sorting
    fun currentFullList(): List<Card> = fullList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val card = getItem(position)
        holder.bind(card)
    }

    override fun submitList(list: List<Card>?) {
        super.submitList(list?.toList())
        fullList = list ?: listOf()
    }

    inner class VH(private val b: ItemCardBinding) : RecyclerView.ViewHolder(b.root) {
        private val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        fun bind(card: Card) {
            try {
                // Set card type text with tvTypeText
                b.tvTypeText?.text = card.type
                b.tvType?.text = card.type
                b.tvNumber.text = card.number
                b.tvOwner.text = card.ownerName
                b.tvAuthority.text = card.authority

                // Format dates
                try {
                    b.tvIssued.text = "Issued: ${sdf.format(Date(card.issuedDate))}"
                } catch (_: Exception) {
                    b.tvIssued.text = "Issued: -"
                }
                try {
                    b.tvExpiry.text = "Expiry: ${sdf.format(Date(card.expiryDate))}"
                } catch (_: Exception) {
                    b.tvExpiry.text = "Expiry: -"
                }

                // Set per-type background, logo, and photo shape using helper
                val ctx = b.root.context
                val template = CardTypeVisuals.getTemplate(card.type)

                // Set background for header
                if (template.backgroundRes != null) {
                    b.bg.background = ContextCompat.getDrawable(ctx, template.backgroundRes)
                } else {
                    b.bg.background = null
                }

                // Set logo badge
                if (template.logoRes != null) {
                    b.logo.setImageDrawable(ContextCompat.getDrawable(ctx, template.logoRes))
                } else {
                    // use a sensible fallback icon instead of clearing the ImageView
                    b.logo.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.id_card_icon))
                }

                // Set shape appearance on photo
                b.ivPhoto.shapeAppearanceModel = CardTypeVisuals.getShapeAppearanceModel(card.type)

                // Load photo with Glide - high quality with proper caching
                val glideRequest = Glide.with(b.root)
                    .load(card.photoUri)
                    .placeholder(R.drawable.placeholder_user)
                    .error(R.drawable.placeholder_user)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)

                if (template.isCircular) {
                    glideRequest.apply(RequestOptions.circleCropTransform()).into(b.ivPhoto)
                } else {
                    glideRequest.apply(RequestOptions.bitmapTransform(CenterCrop()).transform(RoundedCorners(12))).into(b.ivPhoto)
                }

                // Highlight if expiring within 30 days
                val daysToExpire = ((card.expiryDate - System.currentTimeMillis()) / (1000L * 60 * 60 * 24))
                if (daysToExpire in 0..30) {
                    b.root.setCardBackgroundColor(Color.parseColor("#FFEBEE")) // pale red
                } else {
                    b.root.setCardBackgroundColor(Color.WHITE)
                }

                // Clicks - Updated buttons
                b.root.setOnClickListener { onAction(card, Action.VIEW) }
                // Open full image when photo tapped - delegate to host via onAction
                b.ivPhoto.setOnClickListener { onAction(card, Action.VIEW_IMAGE) }
                b.btnView?.setOnClickListener { onAction(card, Action.VIEW) }
                b.btnEdit.setOnClickListener { onAction(card, Action.EDIT) }
                b.btnDelete.setOnClickListener { onAction(card, Action.DELETE) }
                b.btnShare.setOnClickListener { onAction(card, Action.SHARE) }
            } catch (e: Exception) {
                android.util.Log.e("CardAdapter", "Error binding card: ${e.message}", e)
            }
        }
    }


    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val q = constraint?.toString()?.lowercase(Locale.getDefault())?.trim() ?: ""
            val filtered = if (q.isEmpty()) fullList else fullList.filter {
                it.type.lowercase(Locale.getDefault()).contains(q)
                        || it.number.lowercase(Locale.getDefault()).contains(q)
                        || it.authority.lowercase(Locale.getDefault()).contains(q)
            }
            return FilterResults().apply { values = filtered }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            submitList(results?.values as? List<Card> ?: listOf())
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Card, newItem: Card) = oldItem == newItem
        }
    }
}
