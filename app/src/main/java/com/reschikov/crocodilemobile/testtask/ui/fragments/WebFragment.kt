package com.reschikov.crocodilemobile.testtask.ui.fragments

import android.os.Build
import android.os.Bundle
import android.webkit.*
import android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.reschikov.crocodilemobile.testtask.BuildConfig
import com.reschikov.crocodilemobile.testtask.R
import com.reschikov.crocodilemobile.testtask.navigation.Screens
import com.reschikov.crocodilemobile.testtask.network.CheckNetWork
import com.reschikov.crocodilemobile.testtask.ui.viewmodel.LinkViewModel
import com.reschikov.crocodilemobile.testtask.ui.viewmodel.ViewModelFactory
import com.reschikov.crocodilemobile.testtask.utils.showAlertDialog
import kotlinx.android.synthetic.main.fragment_web.*
import org.koin.android.ext.android.get
import ru.terrakok.cicerone.Router

class WebFragment : Fragment(R.layout.fragment_web){

    private val webClient: WebViewClient = object : WebViewClient(){

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
            if (checkRequestUrl(url)){
                if (hasNetWork) viewModel.requestUserLink(url)
                return null
            }
            return super.shouldInterceptRequest(view, url)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
            val url = request.url.toString()
            if (checkRequestUrl(url)){
                if (hasNetWork) viewModel.requestUserLink(url)
                return null
            }
            return super.shouldInterceptRequest(view, request)
        }
    }

    private fun checkRequestUrl(requestUrl: String) : Boolean {
        val userUrl = BuildConfig.URL_TEST
        return requestUrl == userUrl
    }

    private val observerLink by lazy {
        Observer<String?> { if (hasNetWork) loadLink(it)}
    }
    private val observerBreakLoad by lazy {
        Observer<Boolean> { if (it) wv_web.stopLoading() }
    }
    private val observerNetWork by lazy {
        Observer<String?>{
            hasNetWork = it == null
            it?.let{ stopLoadLink(it) } ?: loadLink(viewModel.getLink().value)
        }
    }
    private val observerError by lazy {
        Observer<String?> {
            it?.let { stopLoadLink(it) }
        }
    }
    private var hasNetWork : Boolean = false
    private lateinit var viewModel: LinkViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        get<CheckNetWork>().observe(this, observerNetWork)
        activity?.let {
            viewModel = ViewModelProvider(it, get<ViewModelFactory>()).get(LinkViewModel::class.java)
        }
        wv_web.webViewClient = webClient
        wv_web.settings.cacheMode = LOAD_CACHE_ELSE_NETWORK
        viewModel.getLink().observe(this, observerLink)
        viewModel.isBreakLoad().observe(this, observerBreakLoad)
        viewModel.getErrorMessage().observe(this, observerError)
    }

    private fun stopLoadLink(err: String){
        wv_web.stopLoading()
        activity?.showAlertDialog(R.string.attention, err)
    }

    private fun loadLink(url : String?){
        url?.let { wv_web.loadUrl(it) } ?: get<Router>().replaceScreen(Screens.TinderScreen())
    }

    override fun onResume() {
        super.onResume()
        wv_web.onResume()
    }

    override fun onPause() {
        super.onPause()
        wv_web.onPause()
    }
}