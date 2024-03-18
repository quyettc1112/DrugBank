package com.example.drugbank.di

import com.example.drugbank.common.BaseAPI.BaseAPI
import com.example.drugbank.apiService.API_User_Service
import com.example.drugbank.apiService.Admin_DrugM_APIService
import com.example.drugbank.apiService.Admin_ProductDetail_Service
import com.example.drugbank.apiService.Admin_ProductM_APIService
import com.example.drugbank.apiService.Admin_Profile_APIService
import com.example.drugbank.apiService.Admin_UserM_APIService
import com.google.firebase.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun provideBaseUrl() = BaseAPI.BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTimeout() = BaseAPI.NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()


    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }

        OkHttpClient
            .Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): Admin_UserM_APIService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Admin_UserM_APIService::class.java)


    @Provides
    @Singleton
    fun provideRetrofit_Drug(baseUrl: String, gson: Gson, client: OkHttpClient): Admin_DrugM_APIService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Admin_DrugM_APIService::class.java)


    @Provides
    @Singleton
    fun provideRetrofit_Product(baseUrl: String, gson: Gson, client: OkHttpClient): Admin_ProductM_APIService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Admin_ProductM_APIService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit_ProductDetail(baseUrl: String, gson: Gson, client: OkHttpClient): Admin_ProductDetail_Service =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Admin_ProductDetail_Service::class.java)


    @Provides
    @Singleton
    fun provideRetrofit_UploadImUser(baseUrl: String, gson: Gson, client: OkHttpClient): API_User_Service =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(API_User_Service::class.java)


    @Provides
    @Singleton
    fun provideRetrofit_Profile(baseUrl: String, gson: Gson, client: OkHttpClient): Admin_Profile_APIService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Admin_Profile_APIService::class.java)

}