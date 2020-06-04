package com.zhouzi.retrofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonSyntaxException
import com.orhanobut.logger.Logger
import com.zhouzi.retrofit.http.*
import kotlinx.coroutines.*
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException

class MainActivity : AppCompatActivity() {

    val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launch(
            block = {
                val b = RetrofitFactory.getInstence().api().login("").result()
            },
            error = {
                Logger.d(it)
            }
        )

    }

    fun launch(
        start: (() -> Unit)? = null,
        block: suspend () -> Unit,
        error: ((HttpException) -> Unit)? = null,
        finally: (() -> Unit)? = null
    ) =
        coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    block()
                }
            } catch (e: Exception) {
                Logger.d(e)
                when (e) {
                    is CodeException -> {
                        error(e)
                    }
                    is ConnectException -> showToast("网络连接失败：请检查您的网络后重试\n详细信息：${e.message}")
                    is HttpException ->
                        when (e.code()) {
                            404 -> showToast("网络连接失败：您所请求的资源无法找到\n错误码：${e.code()}\n详细信息：${e.message()}")
                            500 -> showToast("网络连接失败：服务器异常，请稍后再试\n错误码：${e.code()}\n详细信息：${e.message()}")
                            else -> showToast("网络连接失败：网络异常，请稍后再试\n错误码：${e.code()}\n详细信息：${e.message()}")
                        }
                    is IOException -> {
                        e.printStackTrace()
                    }
                    is JSONException -> showToast("数据错误：服务器数据异常\n详细信息：${e.message}")
                    is JsonSyntaxException -> showToast("数据错误：服务器数据异常\n详细信息：${e.message}")
                    is NoSuchFileException -> showToast("文件不存在")
                    else -> showToast("未知错误：未知原因错误\n详细信息：${e.message}")
                }
            } finally {
                finally?.invoke()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }


}