package posidon.onyx

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.gallery.*

class Gallery : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery)
        setSupportActionBar(toolbar)
    }

    fun draw(v: View) {
        startActivity(Intent(this, DrawActivity::class.java))
    }
}
