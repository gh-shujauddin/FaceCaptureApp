package com.qadri.facecapture.data

import com.qadri.facecapture.model.LoginDetails
import com.qadri.facecapture.model.LoginResponse
import com.qadri.facecapture.model.RegisterDetails
import com.qadri.facecapture.model.RegisterResponse
import com.qadri.facecapture.network.FaceDetectionApiService
import retrofit2.Response
import javax.inject.Inject

interface LoginRepository {
    suspend fun login(loginDetails: LoginDetails): LoginResponse

    suspend fun register(registerDetails: RegisterDetails): RegisterResponse
}

class FaceDetectionLoginRepository @Inject constructor(private val retrofitServiceForLoginToken: FaceDetectionApiService) :
    LoginRepository {
    override suspend fun login(loginDetails: LoginDetails): LoginResponse =
        retrofitServiceForLoginToken.login(loginDetails)

    override suspend fun register(registerDetails: RegisterDetails): RegisterResponse =
        retrofitServiceForLoginToken.register(registerDetails)


}