package br.bano.chucknorris.ui.joke

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.bano.chucknorris.R
import br.bano.chucknorris.databinding.JokeFragmentBinding
import br.bano.chucknorris.utils.goneIfMissing
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
        nextButton.apply {
            val animation = AnimationUtils.loadAnimation(context, R.anim.next)
            startAnimation(animation)
        }

        logoImage.loadImageUrl(JokeUiData.ICON_URL)

        viewModel.jokeLiveData.observe(this@JokeFragment, Observer { joke ->
            if (joke == null) return@Observer
            jokeUi = joke
            categoryText.goneIfMissing()
        })

        viewModel.loadJoke()
    }

    companion object {
        fun newInstance() = JokeFragment()
    }
}
