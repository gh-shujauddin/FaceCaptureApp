package com.qadri.facecapture.di

import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.qadri.facecapture.data.AuthRepository
import com.qadri.facecapture.data.AuthRepositoryImpl
import com.qadri.facecapture.data.FaceDetectionLoginRepository
import com.qadri.facecapture.data.LoginRepository
import com.qadri.facecapture.network.FaceDetectionApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

//    private var json = Json { ignoreUnknownKeys = true }
//
//    @Singleton
//    @Provides
//    fun retrofit(): Retrofit = Retrofit.Builder()
//        .baseUrl("https://manyadjustment.backendless.app/api/users/")
//        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//        .build()
//
//    @Provides
//    @Singleton
//    fun provideApiService(retrofit: Retrofit): FaceDetectionApiService = retrofit.create(FaceDetectionApiService::class.java)
//
//    @Provides
//    @Singleton
//    fun provideLoginRepository(retrofitServiceForLoginToken: FaceDetectionLoginRepository): LoginRepository = retrofitServiceForLoginToken

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth: FirebaseAuth) : AuthRepository{
        return AuthRepositoryImpl(firebaseAuth)
    }
}