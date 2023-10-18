package com.qadri.facecapture.ui.facedetection

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Color
import com.google.mlkit.vision.face.Face

class FaceBox(
    overlay: FaceBoxOverlay,
    private val face: Face,
    private val imageRect: Rect
) : FaceBoxOverlay.FaceBox(overlay) {
    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 6.0f
    }

    override fun draw(canvas: Canvas?) {

        val rect = getBoxRect(
            imageRectHeight = imageRect.height().toFloat(),
            imageRectWidth = imageRect.width().toFloat(),
            faceBoundingBox = face.boundingBox
        )

        canvas?.drawRect(rect, paint)
    }
}