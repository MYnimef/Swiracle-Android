package com.mynimef.swiracle.Interfaces;

import android.os.Handler;

import com.mynimef.swiracle.fragments.signup.SignUpFragment;

public interface ISignUp {
    void setBirthday(int year, int month, int day);
    void setEmail(String email);
    void setName(String name);
    void setGender(int gender);
    void setUsername(String username);
    void setPassword(String password);

    void setStage(SignUpFragment.EStage stage);
    void completeRegistration(Handler handler);
}