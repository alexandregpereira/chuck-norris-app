package br.bano.chucknorris.ui.jokes

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.bano.chucknorris.R

class JokesFragment : Fragment() {

    companion object {
        fun newInstance() = JokesFragment()
    }

    private lateinit var viewModel: JokesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.jokes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JokesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
