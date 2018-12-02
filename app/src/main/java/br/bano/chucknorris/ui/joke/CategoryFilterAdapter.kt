package br.bano.chucknorris.ui.joke

import androidx.core.content.ContextCompat
import br.bano.chucknorris.R
import br.bano.chucknorris.databinding.CategoryFilterItemBinding
import com.bano.goblin.adapter.BaseAdapter

class CategoryFilterAdapter(
        categoryFilters: List<CategoryFilterItem>,
        onClick: (CategoryFilterItem) -> Unit
) : BaseAdapter<CategoryFilterAdapter.CategoryFilterItem, CategoryFilterItemBinding>(categoryFilters, R.layout.category_filter_item, onClick) {

    override fun onBindViewHolder(binding: CategoryFilterItemBinding?, categoryFilter: CategoryFilterItem?) {
        if (binding == null || categoryFilter == null) return
        binding.categoryButton.text = categoryFilter.value

        val shapeBackgroundRes = when (categoryFilter.id) {
            ADD_ID -> Pair(R.drawable.add_category_button_selector, R.drawable.ic_add)
            else -> Pair(R.drawable.category_button_selector, R.drawable.ic_close)
        }

        val shapeBackground = ContextCompat.getDrawable(binding.categoryButton.context, shapeBackgroundRes.first)
        binding.categoryButton.background = shapeBackground

        binding.categoryButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, shapeBackgroundRes.second, 0)
    }

    data class CategoryFilterItem(
            val value: String,
            val id: String = value
    )

    companion object {
        const val ADD_ID = "ADD_ID"
    }
}