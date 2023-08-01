package com.example.demo1688

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.demo1688.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bridge: MBridge

    private lateinit var binding: ActivityMainBinding
    private var keyWorld = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initTaoBao()
        initListener()
    }

    private fun initListener(){

        binding.btnSearch.setOnClickListener {
            binding.wbTaoBao.evaluateJavascript("document.querySelector(\"input[type='search']\").value;") {
                    result ->
                binding.wb1688.loadUrl("https://m.1688.com/offer_search/-6D7033.html?keywords=${result.replace("\"","")}")
            }
        }
    }
    private fun initTaoBao() {
        binding.wbTaoBao.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true

        }
        binding.wbTaoBao.webViewClient = customClient
        this.bridge = MBridge(binding.wbTaoBao)

        binding.wb1688.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        binding.wbTaoBao.loadUrl("https://m.intl.taobao.com/search/search.html")
        binding.wb1688.loadUrl("https://m.1688.com/offer_search/-6D7033.html?keywords=")
    }

    private val customClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler,
            error: SslError
        ) {}


        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            val injectScript = "function loadScript(e){var t=document.getElementsByTagName(\"head\")[0],a=document.createElement(\"script\");a.type=\"text/javascript\",a.src=e,t.appendChild(a)}"
            binding.wbTaoBao.evaluateJavascript(injectScript) {
                    result ->

                binding.wbTaoBao.evaluateJavascript("loadScript('https://youtube.codevn.org/parse/taobao.js?t='+Date.now())"){}
            }
        }
    }

}