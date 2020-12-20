package xyz.eddief.halfway.data.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import xyz.eddief.halfway.data.service.MapsService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object MapsRepositoryModule {

    @Singleton
    @Provides
    fun provideMapsRepository(mapsService: MapsService): MapsRepository {
//        return MapsRepositoryMock()
        return MapsRepositoryImpl(mapsService)
    }
}