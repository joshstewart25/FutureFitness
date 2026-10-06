package co.future.exerciseprogress.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    // TODO: Switch to Clock.systemDefaultZone() once the app has current workout data.
    // The bundled sample workouts are all from 2020, so "today" is pinned to a date inside that range.
    @Provides
    @Singleton
    fun provideClock(): Clock {
        return Clock.fixed(Instant.parse("2020-11-24T12:00:00Z"), ZoneId.systemDefault())
    }
}
