package com.saras.pupilmesh.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.saras.pupilmesh.utils.imageRequest

@Composable
fun DetailScreen(viewModel: DetailScreenViewModel = hiltViewModel(), mangaId: String) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchMangaDataById(mangaId) }

    val context = LocalContext.current

    when (val state = uiState) {
        DetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }

        is DetailUiState.Success -> {
            val manga = state.manga
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    Box(
                        modifier = Modifier.size(120.dp, 200.dp)
                    ) {

                        val painter =
                            rememberAsyncImagePainter(imageRequest(context, manga.imageUrl))

                        Image(
                            painter,
                            manga.title,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Text(
                            manga.title.trimStart(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        LazyColumn {
                            item {
                                Text(
                                    manga.subTitle.replace(";", "•"),
                                    fontSize = 12.sp,
                                    softWrap = true
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                LazyColumn {
                    item {
                        Text(manga.summary, fontSize = 14.sp)
                    }
                }
            }
        }
    }

}