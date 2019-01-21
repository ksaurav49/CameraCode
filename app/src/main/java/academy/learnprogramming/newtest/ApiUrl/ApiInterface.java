package academy.learnprogramming.newtest.ApiUrl;

import java.util.List;

import academy.learnprogramming.newtest.response.ImageResponse;
import academy.learnprogramming.newtest.response.ImagepResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("image")
    Call<ImageResponse> image(
            @Field("image") String name,
            @Field("text") String text

    );

    @GET("imagep")
    Call<List<ImagepResponse>> imagep();

}
