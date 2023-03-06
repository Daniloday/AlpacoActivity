package com.missclick.alpacoactivity

import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class ViewClient : WebViewClient() {


    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val intent: Intent
        if (request?.url.toString().startsWith("https:/")) {
            return false
        }
        if (request?.url.toString().startsWith("http:/")) {
            return false
        }
        if (request?.url.toString().startsWith("intent")) {
            intent = Intent.parseUri(request?.url.toString(), Intent.URI_INTENT_SCHEME)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            if (intent.action == "com.google.firebase.dynamiclinks.VIEW_DYNAMIC_LINK") {
                intent.extras?.getString("browser_fallback_url")
                    ?.let { view?.loadUrl(it) }
                return true
            }
        } else {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(request?.url.toString()))
        }
        try {
            view!!.context.startActivity(intent)
        } catch (_: Exception) {
        }
        return true
    }





}