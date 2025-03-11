package tech.gregbuilds.barrowstodoapp.di.modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    //TODO Think about what I want my Repository to look like and do - this needs to interact with my Room database.
//    @Singleton
//    @Provides
//     fun provideTodoRepository(
//        api: TodoApi
//    ): TodoRepository = TodoRepository(api)
}