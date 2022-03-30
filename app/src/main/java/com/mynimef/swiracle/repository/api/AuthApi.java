package com.mynimef.swiracle.repository.api;

import com.mynimef.swiracle.models.Login;
import com.mynimef.swiracle.models.SignUpServer;
import com.mynimef.swiracle.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/auth/signup")
    Call<Boolean> signUp(@Body SignUpServer signUpServer);

    @POST("/auth/signin")
    Call<User> signIn(@Body Login login);
}