package academy.learnprogramming.newtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import academy.learnprogramming.newtest.ApiUrl.ApiInterface;
import academy.learnprogramming.newtest.ApiUrl.ApiUrl;
import academy.learnprogramming.newtest.response.ImagepResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Read extends AppCompatActivity {
    RecyclerView myrcview;
    TestAdapter testAdapter;
    List<ImagepResponse> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        myrcview = findViewById(R.id.rec);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        myrcview.setLayoutManager(layoutManager);
        testAdapter= new TestAdapter(getApplicationContext(),productList);
        myrcview.setAdapter(testAdapter);
        Retrofit retrofi = new Retrofit.Builder()
                .baseUrl(ApiUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiServic = retrofi.create(ApiInterface.class);
        Call<List<ImagepResponse>> cal = apiServic.imagep();
        cal.enqueue(new Callback<List<ImagepResponse>>() {
            @Override
            public void onResponse(Call<List<ImagepResponse>> call, Response<List<ImagepResponse>> response) {

                productList=response.body();

                testAdapter.setProductList(productList);
            }

            @Override
            public void onFailure(Call<List<ImagepResponse>> call, Throwable t) {

            }
        });

    }

}
