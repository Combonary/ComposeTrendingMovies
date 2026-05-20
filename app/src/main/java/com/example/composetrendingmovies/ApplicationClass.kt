package com.example.composetrendingmovies

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.kotlin.imdb.db.applicationContext

@HiltAndroidApp
class ApplicationClass: Application() {
    override fun onCreate() {
        io.github.kotlin.imdb.db.applicationContext = this
        super.onCreate()
    }
}