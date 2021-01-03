package xyz.eddief.halfway.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import xyz.eddief.halfway.utils.SharedPreferencesController
import xyz.eddief.halfway.utils.SharedPreferencesControllerImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreferencesController(@ApplicationContext context: Context): SharedPreferencesController =
        SharedPreferencesControllerImpl(context)
}