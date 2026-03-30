package com.example.interview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.interview.databinding.ItemNameBinding

data class Person(val name: String, val subtitle: String)

class NameAdapter(
    private val fullList: List<Person>,
    private val onCountChanged: (Int) -> Unit
) : RecyclerView.Adapter<NameAdapter.NameViewHolder>(), Filterable {

    /** Colors to cycle through for avatars */
    private val avatarColors = intArrayOf(
        R.color.avatar_0, R.color.avatar_1, R.color.avatar_2, R.color.avatar_3,
        R.color.avatar_4, R.color.avatar_5, R.color.avatar_6, R.color.avatar_7
    )

    private var filteredList: List<Person> = fullList.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val binding = ItemNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.bind(filteredList[position], position)
    }

    override fun getItemCount(): Int = filteredList.size

    inner class NameViewHolder(private val binding: ItemNameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person, position: Int) {
            binding.tvName.text = person.name
            binding.tvSubtitle.text = person.subtitle
            binding.tvInitial.text = person.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

            // Cycle through avatar color palette
            val colorRes = avatarColors[position % avatarColors.size]
            val bgDrawable = binding.avatarContainer.background.mutate() as android.graphics.drawable.GradientDrawable
            bgDrawable.setColor(binding.root.context.getColor(colorRes))

            // Ripple effect on click
            binding.root.setOnClickListener {
                it.animate().scaleX(0.97f).scaleY(0.97f).setDuration(80)
                    .withEndAction { it.animate().scaleX(1f).scaleY(1f).setDuration(80).start() }
                    .start()
            }
        }
    }

    override fun getFilter(): Filter = nameFilter

    private val nameFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val query = constraint?.toString()?.trim()?.lowercase() ?: ""
            results.values = if (query.isEmpty()) {
                fullList.toList()
            } else {
                fullList.filter { it.name.lowercase().contains(query) }
            }
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = (results?.values as? List<Person>) ?: emptyList()
            notifyDataSetChanged()
            onCountChanged(filteredList.size)
        }
    }
}
