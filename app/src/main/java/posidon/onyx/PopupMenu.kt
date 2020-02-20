package posidon.onyx

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View

class PopupMenu(context: Context, src: View, content: Int) : Dialog(context) {

    init {
        window?.setGravity(Gravity.BOTTOM or Gravity.START)
        window?.decorView?.x = src.x
        setContentView(content)
    }
}