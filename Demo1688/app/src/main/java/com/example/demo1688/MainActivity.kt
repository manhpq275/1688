package com.example.demo1688

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Toast
import com.example.demo1688.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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
            keyWorld = binding.edtSearch.text.toString()
            if (keyWorld.isEmpty()) {
                Toast.makeText(this, "Cần nhập từ khóa", Toast.LENGTH_SHORT).show()
            } else {
                //home-link
                binding.wbTaoBao.evaluateJavascript("document.querySelector(\"a[class='serach-btn']\").href;") {
                    result ->
                    binding.wbTaoBao.loadUrl(result)
                }
                //binding.wbTaoBao.evaluateJavascript("document.querySelector(\"input[type='search']\").value = '$keyWorld';document.querySelector(\"span[class='_3hUKWj6b-0Ar6_zQ6_XdqI']\").click();", null)
                binding.wb1688.loadUrl("https://m.1688.com/offer_search/-6D7033.html?keywords=$keyWorld")

            }
        }
    }
    private fun initTaoBao() {
        binding.wbTaoBao.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }
        binding.wb1688.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        binding.wbTaoBao.loadUrl("https://m.intl.taobao.com")
        binding.wb1688.loadUrl("https://m.1688.com/offer_search/-6D7033.html?keywords=")
    }
}