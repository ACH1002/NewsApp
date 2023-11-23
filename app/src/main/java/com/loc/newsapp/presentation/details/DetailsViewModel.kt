package com.loc.newsapp.presentation.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.model.Article
import com.loc.newsapp.domain.usecases.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    var sideEffet by mutableStateOf<String?>(null)
        private set

    fun onEvent(event: DetailsEvent){
        when(event){
            is DetailsEvent.UpsertDeleteArticle -> {
                viewModelScope.launch {
                    Log.d("onEvent", event.article.toString())
                    val article = newsUseCases.selectArticle(event.article.url)
                    if (article == null){
                        upsertArticle(event.article)
                    }else{
                        deleteArticle(event.article)
                    }
                }

            }
            is DetailsEvent.RemoveSideEffect -> {
                sideEffet = null
            }
        }
    }

    private suspend fun upsertArticle(article: Article){
        newsUseCases.upsertArticle(article = article)
        sideEffet = "Article Saved"
    }

    private suspend fun deleteArticle(article: Article){
        newsUseCases.deleteArticle(article = article)
        sideEffet = "Article Deleted"
    }
}