package com.jelazi.sugarmeter

import android.net.http.SslError
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var webPage = "http://google.com"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)



        webView = findViewById(R.id.webview)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.proceed() // Ignore SSL certificate errors
            }
        }

        val settings: WebSettings = webView.getSettings()
        settings.javaScriptEnabled = true
        webView.measure(100, 100)
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        webPage = intent.extras?.get("webPage").toString()


        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = webPage

        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        webView.loadUrl(webPage)


    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}