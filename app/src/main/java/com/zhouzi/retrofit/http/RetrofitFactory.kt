package com.zhouzi.retrofit.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by ZhouZi on 2019/4/8.
 * time:14:59
 * ----------Dragon be here!----------/
 * 　　　┏┓　　 ┏┓
 * 　　┏┛┻━━━┛┻┓━━━
 * 　　┃　　　　　 ┃
 * 　　┃　　　━　  ┃
 * 　　┃　┳┛　┗┳
 * 　　┃　　　　　 ┃
 * 　　┃　　　┻　  ┃
 * 　　┃　　　　   ┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛━━━━━
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━神兽出没━━━━━━━━━━━━━━
 */
class RetrofitFactory private constructor() {

    private var apiFunction: ApiFunction? = null

    init {
        val mOkHttpClient = OkHttpClient.Builder()
            .connectTimeout(HttpConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS) //建立连接 耗时
            .readTimeout(HttpConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(InterceptorUtils.logInterceptor())//添加日志拦截器
            .build()

        apiFunction = Retrofit.Builder()
            .baseUrl(HttpConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()
            .create(ApiFunction::class.java)

    }

    companion object {
        private var mRetrofitFactory: RetrofitFactory? = null
            get() {
                if (field == null) {
                    synchronized(RetrofitFactory::class.java) {
                        if (field == null) {
                            field = RetrofitFactory()
                        }
                    }
                }
                return field!!
            }

        fun getInstence(): RetrofitFactory {
            return mRetrofitFactory!!
        }
    }

    fun api(): ApiFunction = apiFunction!!

}