package com.example.tunisairapp.models;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IapiConnexion {
    @GET("leave/get_all")
    Call<List<DemandeConge>> getRequestLeaves();
    @GET("leave/get_all/{matricule}")
    Call<List<DemandeConge>> getRequestLeavesByMatriculePath(@Path("matricule") String matConge);
    @GET("get_all.php")//...?param=
    Call<List<DemandeConge>> getRequestLeavesByMatriculeQuery(@Query("matrcule") String matrcule);

    @POST("insert.php")
    Call<insertResponse> createRequestLeave(@Body RequestBody demandeConge);

    @Multipart
    @POST("insert.php")
    Call<insertResponse> createRequestLeaveUpload(@Part MultipartBody.Part file,
                                                  @Part("from") RequestBody from,
                                                  @Part("to") RequestBody to,
                                                  @Part("description") RequestBody description,
                                                  @Part("matricule") RequestBody mat,
                                                  @Part("id_type_leave") RequestBody id_type_leave
                                                  );


    @POST("insert.php")
    Call<POST> createRequestLeaves(@Query("from")String from,
                                   @Query("to")String to,
                                   @Query("description")String description,
                                   @Query("justification")String justification,
                                   @Query("matricule")String matricule,
                                   @Query("id_type_leave")String id_type_leave);

    @GET("authorization/get_all")
    Call<List<DemandeAutorisation>> getRequestAuthorization();
    @GET("authorization/get_all/{matricule}")
    Call<List<DemandeAutorisation>> getRequestAuthorizationByMatriculePath(@Path("matricule")String matAuth);
    @GET("get_all.php")
    Call<List<DemandeAutorisation>> getRequestAuthorizationByMatriculQuery(@Query("matrcule")String matrcule);

    @POST("insert.php")
    Call<insertResponse> createRequestAuthorization(@Body RequestBody demandeAuth);

    @POST("insert.php")
    Call<POST> createRequestAuthorization(@Query("from")String from,
                                          @Query("to")String to,
                                          @Query("description")String description,
                                          @Query("matricule")String matricule);

    @FormUrlEncoded
    @POST("")
    Call<POST> createRequestAuthorization(@Field("dateDebut") String dateDebut,
                                          @Field("datefin") String datefin,
                                          @Field("description") String description,
                                          @Field("fichier") String fichier,
                                          @Field("typeConge") String typeConge,
                                          @Field("etatRequestConge") String etatRequestConge
                                          );



    @POST("login.php")
    Call<List<User>> loginUser(@Body RequestBody body
                         );

    @GET("check.php")
    Call<List<User>> checkRequestAuthorization(@Query("matrcule")String matrcule);



}
