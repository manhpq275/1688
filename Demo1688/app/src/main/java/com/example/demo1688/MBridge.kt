package com.example.demo1688

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.json.JSONObject
import java.util.HashMap
import java.util.UUID

typealias BridgeCallBack = ((responseData: String) -> Unit)?

@SuppressLint("SetJavaScriptEnabled")
class MBridge(private val mWebView: WebView) {

    private val messageQueue: HashMap<String, BridgeCallBack> = HashMap()

    init {
        mWebView.settings.javaScriptEnabled = true
        mWebView.addJavascriptInterface(this, "JSBridge")
    }

    @JavascriptInterface
    fun callback(message: String) {
        val result = this.convertToMessageQueued(message)
        val messageId = result.optString("messageId").orEmpty()
        if (messageId.isBlank()) return
        Handler(Looper.getMainLooper()).post {
            messageQueue[messageId]?.invoke(result.optString("data"))
            messageQueue.remove(messageId)
        }
    }

    fun call(handlerName: String, data: String? = null, callBack: BridgeCallBack) {
        val messageId = getMessageId()
        messageQueue[messageId] = callBack
        var command = "javascript:MBrigde.fromNative('$handlerName','$messageId')"
        data?.let {
            command = "javascript:MBrigde.fromNative('$handlerName','$messageId', $it)"
        }
        mWebView.evaluateJavascript(command, null)

    }

    private fun convertToMessageQueued(input: String): JSONObject {
        return JSONObject(input)
    }

    companion object {
        fun getMessageId(): String {
            return UUID.randomUUID().toString()
        }
    }
}

