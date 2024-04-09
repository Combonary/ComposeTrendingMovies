package com.example.composetrendingmovies.domain.model

data class ErrorResponse (
    val errorCode: Int = 0,
    val errorMessage: String? = null
)