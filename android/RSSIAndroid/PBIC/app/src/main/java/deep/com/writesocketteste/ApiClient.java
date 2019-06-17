package deep.com.writesocketteste;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import deep.com.writesocketteste.Models.RSSI8;
import deep.com.writesocketteste.Models.RSSI8List;
import retrofit2.Call;
import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ApiClient {
    private static RSSI8Interface RSSI8Service;

    public static RSSI8Interface getRSSI8Client() {
        if (RSSI8Service == null) {
            Gson gson = new GsonBuilder()
                    //.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();
            Retrofit restAdapter = new Retrofit.Builder()
                    //.baseUrl("https://whispering-springs-87485.herokuapp.com/")
                    .baseUrl("http://192.168.0.11:5000")
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            RSSI8Service = restAdapter.create(RSSI8Interface.class);
        }
        return RSSI8Service;
    }

    public static RSSI8Interface getRSSI8Client(String server) {

            Gson gson = new GsonBuilder()
                    //.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();
            Retrofit restAdapter = new Retrofit.Builder()
                    .baseUrl(server)
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            RSSI8Service = restAdapter.create(RSSI8Interface.class);

        return RSSI8Service;
    }

    public interface RSSI8Interface {
        @GET("/locate")
        Call<String> getLocation(@Query("ap") int[] aps);

        @Headers("Content-type: application/json")
        @POST("/add_local")
        Call<String> putLocation(@Body RSSI8List rssi, @Query("name") String name);
    }
}