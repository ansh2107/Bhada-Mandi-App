package anil.sharma.bhadamandi.rest;


import anil.sharma.bhadamandi.model.Movie;
import anil.sharma.bhadamandi.model.deleteDataModel;
import anil.sharma.bhadamandi.model.getDataModel;
import anil.sharma.bhadamandi.model.getFilterModel;
import anil.sharma.bhadamandi.model.getNameModel;
import anil.sharma.bhadamandi.model.postDataModel;
import anil.sharma.bhadamandi.model.postString;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by dragoon on 03-Nov-16.
 */
public interface API_Interface {

    String URL_EXAMPLE = "http://anshuli.webhostingforstudents.com/mytransport/";

    @FormUrlEncoded
    @POST("postData.php")
    Call<postDataModel> postData(
            @Field("FROM1") String FROM,
            @Field("TO1") String TO,
            @Field("VEHICLE") String VEHICLE,
            @Field("FRIEGHT") String FRIEGHT,
            @Field("REMARK") String REMARK,
            @Field("DATE") String DATE,
            @Field("NAME") String NAME,
            @Field("USERNAME") String USERNAME,
            @Field("CITY") String CITY,
            @Field("MOBILE") String MOBILE,
            @Field("IMAGE_URL") String IMAGE_URL,
            @Field("TIME") String TIME
    );

    @FormUrlEncoded
    @POST("removeData.php")
    Call<deleteDataModel> removeData(
            @Field("S_NO") String S_NO
    );

    @GET("getData.php")
    Call<getDataModel> getDataList(
    );

    @GET("getData.php")
    Call<getNameModel> getNameList(
    );


    @GET("myFilterPlace.php")
    Call<getFilterModel> getNameList1(


    );

    @FormUrlEncoded
    @POST("myFilterName.php")
    Call<getFilterModel> getNameList2(

            @Field("NAME") String NAME
    );


    @FormUrlEncoded
    @POST("register.php")
    Call<postString> postData1(
            @Field("USERNAME") String USERNAME,
            @Field("NAME") String NAME,
            @Field("PASSWORD") String PASSWORD,
            @Field("MOBILE") String MOBILE,
            @Field("EMAIL") String EMAIL,
            @Field("ADDRESS") String ADDRESS,
            @Field("CITY") String CITY,
            @Field("TAX_NO") String TAX_NO,
            @Field("image") String image
    );


    @FormUrlEncoded
    @POST("deleteDate.php")
    Call<postString> postDate(
            @Field("DATE") String DATE
    );

    @GET("login.php")
    Call<Movie> getData(
    );


    class Factory {

        private static API_Interface service = null;

        public static API_Interface getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(URL_EXAMPLE)
                        .build();

                service = retrofit.create(API_Interface.class);
                return service;
            } else {
                return service;
            }
        }

    }
}