package tech.gregbuilds.barrowstodoapp.di.modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Binds
import tech.gregbuilds.barrowstodoapp.util.DateFormatterService
import tech.gregbuilds.barrowstodoapp.util.DefaultDateFormatterService

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    @Singleton
    abstract fun bindDateFormatterService(
        defaultDateFormatterService: DefaultDateFormatterService
    ): DateFormatterService
}