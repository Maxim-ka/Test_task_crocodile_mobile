package com.reschikov.crocodilemobile.testtask.di

import com.reschikov.crocodilemobile.testtask.network.CheckNetWork
import com.reschikov.crocodilemobile.testtask.network.GateWay
import com.reschikov.crocodilemobile.testtask.ui.viewmodel.ViewModelFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

val appModule = module {
    single<Cicerone<Router>> {  Cicerone.create() }
    single { getNavigatorHolder(get()) }
    single { getRouter(get()) }
    single { ViewModelFactory(GateWay(OkHttpClient())) }
    single { CheckNetWork(get()) }
}

private fun getNavigatorHolder(cicerone : Cicerone<Router>) : NavigatorHolder = cicerone.navigatorHolder

private fun getRouter(cicerone : Cicerone<Router>) : Router = cicerone.router