package com.zhouzi.retrofit.http

import android.os.Build
import com.orhanobut.logger.Logger
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by ZhouZi on 2019/4/8.
 * time:15:12
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
object InterceptorUtils {

    fun logInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            private val mMessage = StringBuilder()

            override fun log(message: String) {
                var messageStr = message
                // 请求或者响应开始
                if (messageStr.startsWith("--> GET") || messageStr.startsWith("--> POST")) {
                    mMessage.setLength(0)
                }
                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if (messageStr.startsWith("{") && messageStr.endsWith("}") || messageStr.startsWith(
                        "["
                    ) && messageStr.endsWith(
                        "]"
                    )
                ) {
                    messageStr = JsonUtils.formatJson(JsonUtils.decodeUnicode(messageStr))
                }
                mMessage.append(messageStr + "\n")
                // 响应结束，打印整条日志
                if (messageStr.startsWith("<-- END HTTP")) {
                    Logger.d(mMessage.toString())
                }

            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY)//设置打印数据的级别
}

