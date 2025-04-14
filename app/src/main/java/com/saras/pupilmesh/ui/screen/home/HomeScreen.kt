package com.saras.pupilmesh.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.saras.pupilmesh.data.local.entity.NetworkDataEntity
import com.saras.pupilmesh.utils.imageRequest

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel(), onMangaClicked: (String) -> Unit) {

    val uiState by viewModel.uiState.collectAsState()

    val scrollState = rememberLazyGridState()

    LaunchedEffect(Unit) { viewModel.fetchManga() }

    val fetchNextPage: Boolean by remember {
        derivedStateOf {
            val currentDataCount =
                (uiState as? HomeUiState.Success)?.mangaList?.size ?: return@derivedStateOf false
            val lastDisplayedIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false

            return@derivedStateOf lastDisplayedIndex >= currentDataCount - 10
        }
    }

    LaunchedEffect(fetchNextPage) {
        if (fetchNextPage) viewModel.fetchManga(viewModel.pageNumber + 1)
    }

    when (val state = uiState) {
        HomeUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }

        is HomeUiState.Success -> {
            LazyVerticalGrid(
                state = scrollState,
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(horizontal = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                items(state.mangaList) { manga ->
                    MangaComponent(manga, onMangaClicked)
                }
            }
        }
    }
}

@Composable
fun MangaComponent(data: NetworkDataEntity, onMangaClicked: (String) -> Unit) {

    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(imageRequest(context, data.imageUrl))
    Card(
        onClick = { onMangaClicked(data.id) },
        modifier = Modifier.size(120.dp, 200.dp)
    ) {
        Image(
            painter = painter,
            data.title,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}