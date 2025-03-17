package tech.gregbuilds.barrowstodoapp.di.modules

import android.content.Context
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Binds
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import tech.gregbuilds.barrowstodoapp.util.DateFormatterService
import tech.gregbuilds.barrowstodoapp.util.DefaultDateFormatterService
import tech.gregbuilds.barrowstodoapp.util.TodoNotificationService
import tech.gregbuilds.barrowstodoapp.util.TodoNotificationServiceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    @Singleton
    abstract fun bindDateFormatterService(
        defaultDateFormatterService: DefaultDateFormatterService
    ): DateFormatterService

    @Binds
    @Singleton
    abstract fun bindTodoNotificationService(
        todoNotificationServiceImpl: TodoNotificationServiceImpl
    ): TodoNotificationService

    companion object {
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context {
            return context
        }
    }
}