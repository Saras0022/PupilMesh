package com.saras.pupilmesh.ui.screen.facedetection

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.CameraController.IMAGE_ANALYSIS
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
import com.saras.pupilmesh.utils.FaceDetectorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class FaceDetectionViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val _faceDetectionState =
        MutableStateFlow<FaceDetectionState>(FaceDetectionState.Loading)
    val faceDetectionState = _faceDetectionState.asStateFlow()

    val faceDetectorListener = object : FaceDetectorHelper.DetectorListener {
        override fun onError(error: String, errorCode: Int) {
            _faceDetectionState.update { FaceDetectionState.Error(error) }
        }

        override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {
            val result = flowOf(resultBundle.result)
            viewModelScope.launch {
                result.collectLatest { faceDetectionResult ->
                    _faceDetectionState.update {
                        FaceDetectionState.Success(
                            faceDetectionResult,
                            resultBundle.inputImageHeight,
                            resultBundle.inputImageWidth
                        )
                    }
                }
            }
        }
    }

    private val faceDetectorHelper = FaceDetectorHelper(context, faceDetectorListener)

    val cameraController = LifecycleCameraController(context).apply {
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        imageAnalysisOutputImageFormat = ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
        imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        setEnabledUseCases(IMAGE_ANALYSIS)
        setImageAnalysisAnalyzer(
            Executors.newSingleThreadExecutor(),
            faceDetectorHelper::detectLiveStreamFrame
        )
    }

    override fun onCleared() {
        super.onCleared()
        faceDetectorHelper.clearFaceDetector()
    }
}

sealed interface FaceDetectionState {
    data class Success(val data: FaceDetectorResult, val inputHeight: Int, val inputWidth: Int) :
        FaceDetectionState

    data class Error(val message: String) : FaceDetectionState
    object Loading : FaceDetectionState
}