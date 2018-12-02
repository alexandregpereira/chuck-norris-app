package br.bano.chucknorris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.bano.chucknorris.ui.joke.JokeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, JokeFragment.newInstance())
                .commitNow()
        }
    }

}
