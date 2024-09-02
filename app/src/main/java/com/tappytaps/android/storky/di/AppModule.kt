package com.tappytaps.android.storky.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.tappytaps.android.storky.data.StorkyDatabase
import com.tappytaps.android.storky.data.StorkyDatabaseDao
import com.tappytaps.android.storky.model.StorkyStopwatchState
import com.tappytaps.android.storky.repository.EmailRepository
import com.tappytaps.android.storky.service.PdfCreatorAndSender
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideNotesDao(storkyDatabase: StorkyDatabase): StorkyDatabaseDao =
        storkyDatabase.storkyDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): StorkyDatabase =
        Room.databaseBuilder(
            context,
            StorkyDatabase::class.java,
            "or_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideEmailRepository(): EmailRepository {
        return EmailRepository()
    }

    @Provides
    @Singleton
    fun providePdfCreatorAndSender(): PdfCreatorAndSender {
        return PdfCreatorAndSender()
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("contraction_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideStorkyStopwatchState(): StorkyStopwatchState {
        return StorkyStopwatchState()
    }
}