package br.bano.chucknorris.ui.joke

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.bano.chucknorris.R
import br.bano.chucknorris.databinding.CategoriesDialogBinding
import br.bano.chucknorris.databinding.JokeFragmentBinding
import br.bano.chucknorris.ui.joke.CategoryFilterAdapter.Companion.ADD_ID
import br.bano.chucknorris.utils.createDialog
import br.bano.chucknorris.utils.loadImageUrl

class JokeFragment : Fragment() {

    private lateinit var binding: JokeFragmentBinding

    private val viewModel: JokeViewModel by lazy {
        ViewModelProviders.of(this).get(JokeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.joke_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() = binding.apply {
        nextButton.setOnClickListener { viewModel.loadJoke() }
        previousButton.setOnClickListener { viewModel.loadJoke(previousJoke = true) }

        logoImage.loadImageUrl(JokeUiData.ICON_URL)
        previousButton.isEnabled = false
        nextButton.isEnabled = false

        viewModel.jokeLiveData.observe(this@JokeFragment, Observer { joke ->
            if (joke == null) return@Observer
            jokeUi = joke
            checkButtonsVisibilityByJokePosition(joke)
        })

        viewModel.categoriesLiveData.observe(this@JokeFragment, Observer { categories ->
            if (categories == null) return@Observer
            buildCategoryRecyclerView(categoryFilterRecycler, categories)
        })

        val animation = AnimationUtils.loadAnimation(context, R.anim.next)
        viewModel.loadingLiveData.observe(this@JokeFragment, Observer { loading ->
            if (loading == null) return@Observer
            if (loading) nextButton.startAnimation(animation)
            else nextButton.clearAnimation()

            checkButtonsVisibilityByJokePosition(viewModel.jokeLiveData.value)
        })

        viewModel.loadCategories()
        viewModel.loadJoke()
    }

    private fun checkButtonsVisibilityByJokePosition(joke: JokeUiData?) = binding.apply {
        if (joke == null) return@apply
        previousButton.isEnabled = !viewModel.isFirstJoke(joke)
        nextButton.isEnabled = !viewModel.isLastJoke(joke)
    }

    private fun buildCategoryRecyclerView(categoryFilterRecycler: RecyclerView, categories: List<String>) = categoryFilterRecycler.apply {
        if (categories.isEmpty()) return@apply
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = CategoryFilterAdapter(mutableListOf(
                CategoryFilterAdapter.CategoryFilterItem(
                        "unknown"
                ),
                CategoryFilterAdapter.CategoryFilterItem(
                        getString(R.string.categories),
                        ADD_ID
                )
        )) {
            onCategoryFilterClicked(it, categories)
        }
    }

    private fun onCategoryFilterClicked(categoryFilter: CategoryFilterAdapter.CategoryFilterItem, categories: List<String>) {
        if (categoryFilter.id == ADD_ID) {
            val dialogPair = context?.createDialog<CategoriesDialogBinding>(R.layout.categories_dialog)
            val dialog = dialogPair?.first
            val dialogBinding = dialogPair?.second

            dialogBinding?.categoriesRecycler.apply recyclerApply@{
                if (this == null) return@recyclerApply
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = CategoryDialogAdapter(categories) { category ->
                    dialog?.dismiss()
                    addCategoryFilter(category)
                }
            }

            dialog?.show()

            return
        }

        removeCategoryFilter(categoryFilter)
    }

    private fun removeCategoryFilter(categoryFilter: CategoryFilterAdapter.CategoryFilterItem) {
        val adapter = binding.categoryFilterRecycler.adapter as CategoryFilterAdapter
        if (adapter.itemCount == 2) {
            Toast.makeText(context, R.string.category_filter_remove_warning, Toast.LENGTH_LONG).show()
            return
        }
        adapter.remove(categoryFilter)
        viewModel.removeCategoryFilter(categoryFilter.value)
    }

    private fun addCategoryFilter(category: String) {
        val adapter = binding.categoryFilterRecycler.adapter as CategoryFilterAdapter
        adapter.setItemAtIndex(0, CategoryFilterAdapter.CategoryFilterItem(category))
        viewModel.addCategoryFilter(category)
    }

    companion object {
        fun newInstance() = JokeFragment()
    }
}
