package posidon.onyx.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import posidon.onyx.DrawActivity
import java.lang.Exception

import java.util.HashMap

class Paper(internal var context: Context, attrs: AttributeSet) : View(context, attrs) {


    var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var paintScreen: Paint? = null
    private var pathMap: HashMap<Int, Path>? = null
    private var pointMap: HashMap<Int, Point>? = null
    var line: Paint

    init {
        paintScreen = Paint()
        line = Paint()
        line.isAntiAlias = true
        line.color = 0xff000000.toInt()
        line.strokeWidth = 20f
        line.strokeCap = Paint.Cap.ROUND
        line.style = Paint.Style.STROKE
        pathMap = HashMap()
        pointMap = HashMap()
        setBackgroundColor(-0x1)
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        bitmap!!.eraseColor(0x0)
    }

    override fun onDraw(c: Canvas) {
        c.drawBitmap(bitmap!!, 0f, 0f, paintScreen)
        for (k in pathMap!!.keys)
            canvas!!.drawPath(pathMap!![k]!!, line)
    }

    fun clear() {
        pathMap!!.clear()
        pointMap!!.clear()
        bitmap!!.eraseColor(0x0)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        val actionIndex = event.actionIndex
        var ret = false

        if (action == MotionEvent.ACTION_DOWN) {
            touchStarted(event.getX(actionIndex), event.getY(actionIndex), event.getPointerId(actionIndex))
            ret = true
        } else if (action == MotionEvent.ACTION_UP) {
            touchEnded(event.getPointerId(actionIndex))
            ret = true
        } else if (action == MotionEvent.ACTION_MOVE && !DrawActivity.transformMode) {
            touchMoved(event)
            ret = true
        }

        invalidate()
        return ret
    }

    private fun touchMoved(event: MotionEvent) {
        for (i in 0 until event.pointerCount) {
            val pointerId = event.getPointerId(i)
            val pointerIndex = event.findPointerIndex(pointerId)
            if (pathMap!!.containsKey(pointerId)) {
                val newX = event.getX(pointerIndex)
                val newY = event.getY(pointerIndex)
                val path = pathMap!![pointerId]
                val point = pointMap!![pointerId]
                val deltaX = Math.abs(newX - point!!.x)
                val deltaY = Math.abs(newY - point.y)
                if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
                    path!!.quadTo(point.x.toFloat(), point.y.toFloat(), (newX + point.x) / 2, (newY + point.y) / 2)
                    point.x = newX.toInt()
                    point.y = newY.toInt()
                }
            }
        }
    }

    private fun touchEnded(pointerId: Int) {
        try {
            val path = pathMap!![pointerId]
            canvas!!.drawPath(path!!, line)
            path.reset()
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun touchStarted(x: Float, y: Float, pointerId: Int) {
        val path: Path?
        val point: Point?

        if (pathMap!!.containsKey(pointerId)) {
            path = pathMap!![pointerId]
            point = pointMap!![pointerId]
        } else {
            path = Path()
            pathMap!![pointerId] = path
            point = Point()
            pointMap!![pointerId] = point
        }

        path!!.moveTo(x, y)
        point!!.x = x.toInt()
        point.y = y.toInt()
    }

    companion object {
        val TOUCH_TOLERANCE = 2f
    }
}
