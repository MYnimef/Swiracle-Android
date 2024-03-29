package com.mynimef.swiracle.repository;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.mynimef.swiracle.models.Post;
import com.mynimef.swiracle.models.PostDetails;
import com.mynimef.swiracle.models.SignInRequest;
import com.mynimef.swiracle.models.ProfileView;
import com.mynimef.swiracle.models.SignInCallback;
import com.mynimef.swiracle.models.SignUpRequest;
import com.mynimef.swiracle.models.User;
import com.mynimef.swiracle.repository.api.AuthAPI;
import com.mynimef.swiracle.repository.api.ParsingAPI;
import com.mynimef.swiracle.repository.api.PostAPI;
import com.mynimef.swiracle.models.ClothesParsingInfo;
import com.mynimef.swiracle.models.PostServer;
import com.mynimef.swiracle.repository.api.UserAPI;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

final class NetworkService {
    private final AuthAPI authApi;
    private final PostAPI postApi;
    private final ParsingAPI parsingApi;
    private final UserAPI userApi;
    private final Repository repository;

    NetworkService(Repository repository) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://91.203.192.140:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.authApi = retrofit.create(AuthAPI.class);
        this.postApi = retrofit.create(PostAPI.class);
        this.parsingApi = retrofit.create(ParsingAPI.class);
        this.userApi = retrofit.create(UserAPI.class);

        this.repository = repository;
    }

    void signUp(SignUpRequest request, Handler signUpHandler) {
        Message msg = new Message();
        authApi.signUp(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<SignInCallback> call,
                    @NotNull Response<SignInCallback> response
            ) {
                SignInCallback callback = response.body();

                if (callback != null) {
                    msg.arg1 = 0; // success
                    repository.insertUser(new User(
                            request.getUsername(),
                            callback.getToken(),
                            callback.getPermission()
                    ));
                } else {
                    msg.arg1 = 1; // failure
                }
                signUpHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(
                    @NotNull Call<SignInCallback> call,
                    @NotNull Throwable t
            ) {
                msg.arg1 = -1; // no connection
                signUpHandler.sendMessage(msg);
            }
        });
    }

    void signIn(SignInRequest request, Handler handler) {
        Message msg = new Message();
        authApi.signIn(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<SignInCallback> call,
                    @NotNull Response<SignInCallback> response
            ) {
                SignInCallback callback = response.body();

                if (callback != null) {
                    repository.insertUser(new User(
                            request.getUsername(),
                            callback.getToken(),
                            callback.getPermission()
                    ));
                    msg.arg1 = 0;
                } else {
                    msg.arg1 = 1;
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(
                    @NotNull Call<SignInCallback> call,
                    @NotNull Throwable t
            ) {
                msg.arg1 = -1;
                handler.sendMessage(msg);
            }
        });
    }

    void getPosts(Handler handler) {
        Message msg = new Message();
        postApi.getAll().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<List<Post>> call,
                    @NotNull Response<List<Post>> response
            ) {
                if (response.isSuccessful()) {
                    repository.insertAllPosts(response.body());
                    msg.arg1 = 0;
                } else {
                    msg.arg1 = 1;
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(
                    @NotNull Call<List<Post>> call,
                    @NotNull Throwable t
            ) {
                msg.arg1 = -1;
                handler.sendMessage(msg);
            }
        });
    }

    void getPostsAuth(String token, Handler handler) {
        Message msg = new Message();
        postApi.getAllAuth(token).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<List<Post>> call,
                    @NotNull Response<List<Post>> response
            ) {
                if (response.isSuccessful()) {
                    repository.insertAllPosts(response.body());
                    msg.arg1 = 0;
                } else {
                    msg.arg1 = 1;
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(
                    @NotNull Call<List<Post>> call,
                    @NotNull Throwable t
            ) {
                msg.arg1 = -1;
                handler.sendMessage(msg);
            }
        });
    }

    void likePost(String id, String token) {
        postApi.likePost(token, id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<Boolean> call,
                    @NotNull Response<Boolean> response
            ) {

            }

            @Override
            public void onFailure(
                    @NotNull Call<Boolean> call,
                    @NotNull Throwable t
            ) {

            }
        });
    }

    void getPostDetails(String id, Handler handler) {
        Message message = new Message();
        postApi.getPostDetails(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<PostDetails> call,
                    @NotNull Response<PostDetails> response
            ) {
                if (response.body() != null) {
                    message.arg1 = 0; // success;
                    message.obj = response.body();
                } else {
                    message.arg1 = 1; // failure
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(
                    @NotNull Call<PostDetails> call,
                    @NotNull Throwable t
            ) {
                message.arg1 = -1;
                handler.sendMessage(message); // no connection
            }
        });
    }

    void putPost(PostServer post, List<String> uriList, String token) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (String uri : uriList) {
            File file = new File(uri);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            partList.add(MultipartBody.Part.createFormData("images",
                    file.getName(), requestFile));
        }

        postApi.putPost(token,
                post, partList).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<Boolean> call,
                    @NotNull Response<Boolean> response
            ) {
            }

            @Override
            public void onFailure(
                    @NotNull Call<Boolean> call,
                    @NotNull Throwable t
            ) {
                t.printStackTrace();
            }
        });
    }

    void deletePost(
            String postId,
            Handler handler,
            String token
    ) {
        postApi.deletePost(token, postId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<Boolean> call,
                            @NonNull Response<Boolean> response
                    ) {
                        Message msg = new Message();

                        if (response.body() != null && response.body()) {
                            repository.deletePostLocal(postId);
                            msg.arg1 = 0;
                        } else {
                            msg.arg1 = 1;
                        }

                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<Boolean> call,
                            @NonNull Throwable t
                    ) {
                        Message msg = new Message();
                        msg.arg1 = -1; // no connection
                        handler.sendMessage(msg);
                    }
                });
    }

    void getClothesParsing(String url, Handler handler, String token) {
        parsingApi.getClothesElementParsing(token, url.replaceAll("/", "SWIRACLE"))
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(
                            @NotNull Call<ClothesParsingInfo> call,
                            @NotNull Response<ClothesParsingInfo> response
                    ) {
                        Message msg = new Message();
                        if (response.body() != null) {
                            msg.arg1 = 0; // success
                            msg.obj = response.body();
                        } else {
                            msg.arg1 = 1; // failure
                        }
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(
                            @NotNull Call<ClothesParsingInfo> call,
                            @NotNull Throwable t
                    ) {
                        Message msg = new Message();
                        msg.arg1 = -1; // no connection
                        handler.sendMessage(msg);
                    }
                });
    }

    void followUser(String token, String id) {
        userApi.followUser(token, id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<Boolean> call,
                    @NotNull Response<Boolean> response
            ) {

            }

            @Override
            public void onFailure(
                    @NotNull Call<Boolean> call,
                    @NotNull Throwable t
            ) {

            }
        });
    }

    void getProfileView(String id, Handler handler) {
        Message message = new Message();
        userApi.getProfileView(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<ProfileView> call,
                    @NotNull Response<ProfileView> response
            ) {
                if (response.body() != null) {
                    message.arg1 = 0; // success;
                    message.obj = response.body();
                } else {
                    message.arg1 = 1; // failure
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(
                    @NotNull Call<ProfileView> call,
                    @NotNull Throwable t
            ) {
                message.arg1 = -1;
                handler.sendMessage(message); // no connection
            }
        });
    }

    void getProfileViewAuth(String id, Handler handler, String token) {
        Message message = new Message();
        userApi.getProfileViewAuth(token, id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NotNull Call<ProfileView> call,
                    @NotNull Response<ProfileView> response
            ) {
                if (response.body() != null) {
                    message.arg1 = 0; // success;
                    message.obj = response.body();
                } else {
                    message.arg1 = 1; // failure
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(
                    @NotNull Call<ProfileView> call,
                    @NotNull Throwable t
            ) {
                message.arg1 = -1;
                handler.sendMessage(message); // no connection
            }
        });
    }
}