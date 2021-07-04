package xyz.eddief.halfway.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.eddief.halfway.data.service.FirestoreService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirestoreModule {

    @Singleton
    @Provides
    fun provideFirestoreService(): FirestoreService = FirestoreService(Firebase.firestore)
}