package com.missclick.alpacoactivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File

class AlpacoActivity : AppCompatActivity() {

    private lateinit var missView : WebView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alpaco)
        missView = findViewById(R.id.missView)
        missView.apply {
            settings.mixedContentMode = 0
            webViewClient = ViewClient()
            settings.allowFileAccessFromFileURLs = true
            val instance: CookieManager = CookieManager.getInstance()
            webChromeClient = Chrome{
                requestPermissions(arrayOf(Manifest.permission.CAMERA, ), 354)
            }
            settings.allowFileAccess = true
            instance.setAcceptCookie(true)
            instance.setAcceptThirdPartyCookies(this, true)
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.allowContentAccess = true
            settings.allowUniversalAccessFromFileURLs = true
            settings.useWideViewPort = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            loadUrl(intent.getStringExtra("url").toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != -1){
            Data.valCall?.onReceiveValue(null)
        }else{
            if (data?.data != null){
                Data.valCall?.onReceiveValue(arrayOf(data.data!!))
            }else{
                Data.valCall?.onReceiveValue(arrayOf(Data.pare))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val action1 = Intent(Intent.ACTION_CHOOSER)
        val action2 = Intent(Intent.ACTION_GET_CONTENT)
        val action3 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            action2.addCategory(Intent.CATEGORY_OPENABLE)
            val file = File.createTempFile("pic", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            action2.type = "*/*"
            Data.pare =  FileProvider.getUriForFile(this,
                "com.alpaco",file)
            action3.putExtra(MediaStore.EXTRA_OUTPUT,Data.pare)
            action1.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(action3))
            action1.putExtra(Intent.EXTRA_INTENT, action2)
            startActivityForResult(action1,87)
        }else{
            Data.valCall?.onReceiveValue(null)
        }
    }


    override fun onBackPressed() {
        if (missView.canGoBack()) missView.goBack()
    }

}