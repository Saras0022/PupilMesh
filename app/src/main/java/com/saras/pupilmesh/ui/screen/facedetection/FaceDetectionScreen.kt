package com.saras.pupilmesh.ui.screen.facedetection

import android.annotation.SuppressLint
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner

@SuppressLint("CoroutineCreationDuringComposition", "UnusedBoxWithConstraintsScope")
@Composable
fun FaceDetectionScreen(viewModel: FaceDetectionViewModel = hiltViewModel()) {

    val faceDetectionState by viewModel.faceDetectionState.collectAsState()

    var borderColor by remember { mutableStateOf(Color.Red) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = viewModel.cameraController
    cameraController.bindToLifecycle(lifecycleOwner)


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .requiredSize(300.dp, 400.dp)
                .border(width = 2.dp, color = borderColor)
        ) {

            AndroidView(factory = { ctx ->
                PreviewView(ctx).apply {
                    controller = cameraController
                    keepScreenOn = true
                }
            }, modifier = Modifier.fillMaxSize())

            when (val state = faceDetectionState) {
                is FaceDetectionState.Error -> {}
                FaceDetectionState.Loading -> {}
                is FaceDetectionState.Success -> {
                    val detections = state.data.detections()
                    val inputHeight = state.inputHeight
                    val inputWidth = state.inputWidth

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawWithContent({
                                if (detections.isEmpty()) borderColor = Color.Red
                                for (detection in detections) {

                                    val scaleX = size.width / inputWidth
                                    val scaleY = size.height / inputHeight

                                    borderColor = Color.Green

                                    val boundingBox = detection.boundingBox()
                                    val topLeft =
                                        Offset(boundingBox.left * scaleX, boundingBox.top * scaleY)
                                    val size =
                                        Size(
                                            boundingBox.width() * scaleX,
                                            boundingBox.height() * scaleY
                                        )

                                    drawRect(
                                        Color.Black,
                                        topLeft,
                                        size,
                                        style = Stroke(width = 1f)
                                    )
                                }
                            })
                    )
                }
            }
        }


    }
}