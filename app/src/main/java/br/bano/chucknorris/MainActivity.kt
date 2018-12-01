package br.bano.chucknorris

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.bano.chucknorris.ui.jokes.JokesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, JokesFragment.newInstance())
                .commitNow()
        }
    }

}
