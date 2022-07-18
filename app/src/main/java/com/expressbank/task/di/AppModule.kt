package com.expressbank.task.di

import android.content.Context
import androidx.room.Room
import com.expressbank.task.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context:Context,
        callback: TaskDatabase.Callback
    ) = Room.databaseBuilder(context, TaskDatabase::class.java, "task_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideCategoryDao(db: TaskDatabase) = db.categoryDao()

    @Provides
    fun provideCardDao(db: TaskDatabase) = db.cardDao()

    @Provides
    fun provideYearDao(db: TaskDatabase) = db.yearDao()

    @Provides
    fun provideMonthDao(db: TaskDatabase) = db.monthDao()

    @Provides
    fun provideBudgetDao(db: TaskDatabase) = db.budgetDao()

    @Provides
    fun provideExpaceDao(db: TaskDatabase) = db.expenseDao()

    @Provides
    fun provideExpacehistoryDao(db: TaskDatabase) = db.expenseHistoryDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope