package com.saras.pupilmesh.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock
import androidx.camera.core.ImageProxy
import androidx.core.graphics.createBitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult

class FaceDetectorHelper(
    private val context: Context,
    val faceDetectorListener: DetectorListener? = null
) {

    private lateinit var faceDetector: FaceDetector

    init {
        setupFaceDetector()
    }

    fun setupFaceDetector() {
        val model = "blaze_face_short_range.tflite"

        val baseOptionsBuilder = BaseOptions.builder().setModelAssetPath(model)
        val baseOptions = baseOptionsBuilder.build()

        val optionsBuilder = FaceDetector.FaceDetectorOptions.builder()
            .setBaseOptions(baseOptions)
            .setResultListener(this::returnLiveStreamResult)
            .setErrorListener(this::returnLiveStreamError)
            .setMinDetectionConfidence(0.7f)
            .setRunningMode(RunningMode.LIVE_STREAM)

        val options = optionsBuilder.build()

        faceDetector = FaceDetector.createFromOptions(context, options)
    }

    fun clearFaceDetector() = faceDetector.close()

    fun detectLiveStreamFrame(imageProxy: ImageProxy) {
        val bitmapBuffer =
            createBitmap(imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888)

        imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
        imageProxy.close()

        val matrix = Matrix().apply {
            postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
            postScale(
                -1f,
                1f,
                imageProxy.width.toFloat(),
                imageProxy.height.toFloat()
            )
        }

        val rotatedBitmap = Bitmap.createBitmap(
            bitmapBuffer,
            0,
            0,
            bitmapBuffer.width,
            bitmapBuffer.height,
            matrix,
            true
        )

        val mpImage = BitmapImageBuilder(rotatedBitmap).build()
        val frameTime = SystemClock.uptimeMillis()

        faceDetector.detectAsync(mpImage, frameTime)
    }

    private fun returnLiveStreamResult(
        result: FaceDetectorResult,
        input: MPImage
    ) {
        faceDetectorListener?.onResults(ResultBundle(result, input.height, input.width))
    }

    private fun returnLiveStreamError(error: RuntimeException) {
        faceDetectorListener?.onError(error.message ?: "Unknown error occurred")
    }

    data class ResultBundle(
        val result: FaceDetectorResult,
        val inputImageHeight: Int,
        val inputImageWidth: Int
    )

    interface DetectorListener {
        fun onError(error: String, errorCode: Int = 0)
        fun onResults(resultBundle: ResultBundle)
    }
}