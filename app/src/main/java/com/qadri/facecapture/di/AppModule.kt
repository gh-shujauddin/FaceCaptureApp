package com.qadri.facecapture.di

import com.google.firebase.auth.FirebaseAuth
import com.qadri.facecapture.data.AuthRepository
import com.qadri.facecapture.data.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth: FirebaseAuth) : AuthRepository{
        return AuthRepositoryImpl(firebaseAuth)
    }
}