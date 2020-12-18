package com.example.informationcep;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface RetrofitService {
    @Headers({
            "Accept: application/json"
    })

    @GET("{cep}/json")
    Call<Cep> enviarCep(@Path("cep") String cep);

}
