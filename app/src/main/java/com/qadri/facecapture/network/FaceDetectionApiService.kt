package com.qadri.facecapture.network

import com.qadri.facecapture.model.LoginDetails
import com.qadri.facecapture.model.LoginResponse
import com.qadri.facecapture.model.RegisterDetails
import com.qadri.facecapture.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FaceDetectionApiService {

    @POST("users/login")
//    @Headers(
//        "content-type: application/json",
//        "application-id: D1AA70A5-B00F-410E-FF76-0A13A861DA00",
//        "api-key: A5605B89-58B4-4BB7-9F22-613D65989A5A",
//        "application-type: REST"
//    )
    suspend fun login(@Body loginDetails: LoginDetails): LoginResponse


    @Headers(
        "Content-Type: application/json",
        "application-id: D1AA70A5-B00F-410E-FF76-0A13A861DA00",
        "api-key: A5605B89-58B4-4BB7-9F22-613D65989A5A"
    )
    @POST("register")
    suspend fun register(@Body registerDetails: RegisterDetails): RegisterResponse
}