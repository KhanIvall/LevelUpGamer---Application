package com.example.level_up_gamer.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.level_up_gamer.viewmodel.PostViewModel

@Composable
fun PostScreen(){
    val viewModel: PostViewModel = viewModel()
    val posts by viewModel.posts.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.cargarPost()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Listado de Posts",
            fontSize = 24.sp,
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(posts) { post ->
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = post.title,
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = post.body.take(50) + "...",
                        color = Color.Blue
                    )
                }
            }
        }
    }
}