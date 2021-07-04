package xyz.eddief.halfway.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xyz.eddief.halfway.data.AppDatabase
import xyz.eddief.halfway.data.repository.MapsRepository
import xyz.eddief.halfway.data.repository.MapsRepositoryImpl
import xyz.eddief.halfway.data.repository.UserRepository
import xyz.eddief.halfway.data.repository.UserRepositoryImpl
import xyz.eddief.halfway.data.service.FirestoreService
import xyz.eddief.halfway.data.service.MapsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMapsRepository(mapsService: MapsService): MapsRepository {
//        return FakeMapsRepository()
        return MapsRepositoryImpl(mapsService)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        appDatabase: AppDatabase,
        firestoreService: FirestoreService
    ): UserRepository {
//        return FakeUserRepository()
        return UserRepositoryImpl(appDatabase, firestoreService)
    }
}