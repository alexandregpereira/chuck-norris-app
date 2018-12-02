package br.bano.chucknorris.ui.joke

import br.bano.chucknorris.R
import br.bano.chucknorris.databinding.CategoryItemBinding
import com.bano.goblin.adapter.BaseAdapter

class CategoryDialogAdapter(
        categories: List<String>,
        onClick: (String) -> Unit
) : BaseAdapter<String, CategoryItemBinding>(categories, R.layout.category_item, onClick) {

    override fun onBindViewHolder(binding: CategoryItemBinding?, category: String?) {
        if (binding == null || category == null) return
        binding.categoryText.text = category
    }

}