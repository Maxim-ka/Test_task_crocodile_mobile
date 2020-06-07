package com.reschikov.crocodilemobile.testtask.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.reschikov.crocodilemobile.testtask.R
import com.reschikov.crocodilemobile.testtask.USER_LINK
import com.reschikov.crocodilemobile.testtask.navigation.Screens
import com.reschikov.crocodilemobile.testtask.ui.viewmodel.LinkViewModel
import com.reschikov.crocodilemobile.testtask.ui.viewmodel.ViewModelFactory
import com.reschikov.crocodilemobile.testtask.utils.createShowAsUrl
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_link.*
import org.koin.android.ext.android.get
import ru.terrakok.cicerone.Router



class LinkFragment : Fragment(R.layout.fragment_link) {

    private val listenerUrl =  View.OnClickListener {
        val url = (it as TextView).text.toString()
        viewModel.setLink(url)
        sendToAppMetrica(url)
        router?.replaceScreen(Screens.WebScreen())
        saveLinkUser(url)
    }
    private var router : Router? = null
    private lateinit var viewModel: LinkViewModel

    private fun sendToAppMetrica(url: String){
        YandexMetrica.reportEvent(USER_LINK, mapOf( USER_LINK to url))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        router = get()
        tv_test1.createShowAsUrl()
        tv_test2.createShowAsUrl()
        activity?.let {
            viewModel = ViewModelProvider(it, get<ViewModelFactory>()).get(LinkViewModel::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
        tv_test1.setOnClickListener(listenerUrl)
        tv_test2.setOnClickListener(listenerUrl)
    }

    private fun saveLinkUser(url : String){
        val edit = get<SharedPreferences>().edit()
        edit.putString(USER_LINK, url)
        edit.apply()
    }

    override fun onStop() {
        super.onStop()
        tv_test1.setOnClickListener(null)
        tv_test2.setOnClickListener(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        router = null
    }
}