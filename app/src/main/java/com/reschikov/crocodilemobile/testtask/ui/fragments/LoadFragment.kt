package com.reschikov.crocodilemobile.testtask.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.reschikov.crocodilemobile.testtask.BuildConfig
import com.reschikov.crocodilemobile.testtask.R
import com.reschikov.crocodilemobile.testtask.USER_LINK
import com.reschikov.crocodilemobile.testtask.navigation.Screens
import com.reschikov.crocodilemobile.testtask.network.CheckNetWork
import com.reschikov.crocodilemobile.testtask.ui.viewmodel.LinkViewModel
import com.reschikov.crocodilemobile.testtask.ui.viewmodel.ViewModelFactory
import com.reschikov.crocodilemobile.testtask.utils.createShowAsUrl
import com.reschikov.crocodilemobile.testtask.utils.showAlertDialog
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_load.*
import org.koin.android.ext.android.get
import ru.terrakok.cicerone.Router

private const val WAITING_TIME = 5_000L
private const val INTERVAL = WAITING_TIME + 1_000L
private const val URL_PRIVACY_POLICY = "https://www.freeprivacypolicy.com/our-privacy-policy"

class LoadFragment : Fragment(R.layout.fragment_load) {

    private val countDownTimer = object : CountDownTimer(WAITING_TIME, INTERVAL) {
        override fun onFinish() {
            goOver()
        }

        override fun onTick(millisUntilFinished: Long) {}
    }
    private val observerNetWork by lazy {
        Observer<String?>{
            hasNoNetWork = it != null
            it?.let{
                countDownTimer.cancel()
                activity?.showAlertDialog(R.string.attention, it)
            } ?: countDownTimer.start()
        }
    }
    private var router : Router? = null
    private var hasNoNetWork : Boolean = true
    private lateinit var viewModel: LinkViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        router = get()
        get<CheckNetWork>().observe(this, observerNetWork)
        tv_policy.createShowAsUrl()
        activity?.let {
            viewModel = ViewModelProvider(it, get<ViewModelFactory>()).get(LinkViewModel::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
            but_start.setOnClickListener { goOver() }
            tv_policy.setOnClickListener {
                if (hasNoNetWork) return@setOnClickListener
                viewModel.setLink(URL_PRIVACY_POLICY)
                router?.navigateTo(Screens.WebScreen())
            countDownTimer.start()
        }
    }

    private fun goOver(){
        if (hasNoNetWork) return
        val link = BuildConfig.URL_TEST
        viewModel.setLink(link)
        YandexMetrica.reportEvent(USER_LINK, mapOf( USER_LINK to link))
        router?.replaceScreen(Screens.WebScreen())
    }

    override fun onStop() {
        super.onStop()
        countDownTimer.cancel()
        but_start.setOnClickListener(null)
        tv_policy.setOnClickListener(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        router = null
    }
}