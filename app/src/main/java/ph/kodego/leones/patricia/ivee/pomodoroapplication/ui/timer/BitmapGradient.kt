package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

import android.graphics.*

class GradientTransform(private val originalBitmap: Bitmap) {
//Supposed to be pixelated gradient effect on background. Probably will delete it
    fun createGradientBitmap(): Bitmap {
        val width = originalBitmap.width
        val height = originalBitmap.height


        val scaleFactor = 4

        val smallBitmap = Bitmap.createScaledBitmap(originalBitmap, width / scaleFactor, height / scaleFactor, false)

        val gradientBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(gradientBitmap)
        val paint = Paint()

        val gradient = LinearGradient(0f, 0f, 0f, height.toFloat(), Color.RED, Color.YELLOW, Shader.TileMode.CLAMP)
        paint.shader = gradient
        paint.isFilterBitmap = true
        paint.isDither = true

        canvas.drawBitmap(smallBitmap, 0f, 0f, paint)
        smallBitmap.recycle()

        return gradientBitmap
    }
}