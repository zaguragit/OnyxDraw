package posidon.onyx

import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import posidon.onyx.view.Paper
import kotlin.math.sqrt

class DrawActivity : AppCompatActivity() {

    private lateinit var paper: Paper
    internal var oldmid: PointF? = null
    internal var mid = PointF()
    internal var oldspacing: Float = -1f
    internal var newspacing: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawscreen)
        paper = findViewById(R.id.paper)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.actionMasked) {
            MotionEvent.ACTION_UP -> if (transformMode) {
                transformMode = false
                oldspacing = -1f
                oldmid = null
            }
            MotionEvent.ACTION_POINTER_UP -> run {
                transformMode = false
                oldspacing = -1f
                oldmid = null
                if (paper.scaleX < minScale || paper.scaleY < minScale) paper.animate().scaleX(minScale).scaleY(minScale)
                    .duration = 250L
            }
            MotionEvent.ACTION_POINTER_DOWN -> run {
                transformMode = true
                return true
            }
            MotionEvent.ACTION_MOVE -> if (transformMode) run {
                midPoint(mid, event)
                if (oldmid == null) oldmid = PointF(mid.x, mid.y)
                paper.translationX = paper.translationX + mid.x - oldmid!!.x
                paper.translationY = paper.translationY + mid.y - oldmid!!.y
                //paper.pivotX = paper.pivotX + mid.x - oldmid!!.x
                //paper.pivotY = paper.pivotY + mid.y - oldmid!!.y
                oldmid!!.set(mid)

                newspacing = spacing(event)
                if (oldspacing == -1f) {
                    oldspacing = newspacing
                }
                val scale = newspacing / oldspacing
                paper.scaleX *= scale
                paper.scaleY *= scale
                oldspacing = newspacing
                return true
            }
        }
        return false
    }

    fun showBrushMenu(v: View) {
        paper.clear()
    }

    fun showColorMenu(v: View) {
        val d: PopupMenu = PopupMenu(this, v, R.layout.drawscreen)
    }

    fun dropper(v: View) {

    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        point.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2)
    }

    companion object {
        var transformMode: Boolean = false
        val minScale = 0.36f
    }
}
