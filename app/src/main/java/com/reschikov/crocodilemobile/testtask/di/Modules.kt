package com.reschikov.crocodilemobile.testtask.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.reschikov.crocodilemobile.testtask.network.CheckNetWork
import com.reschikov.crocodilemobile.testtask.network.GateWay
import com.reschikov.crocodilemobile.testtask.ui.viewmodel.ViewModelFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

private const val USER = "user"

val appModule = module {
    single<Cicerone<Router>> {  Cicerone.create() }
    single { getNavigatorHolder(get()) }
    single { getRouter(get()) }
    single { getPreference(get()) }
    single { ViewModelFactory(GateWay(OkHttpClient())) }
    single { CheckNetWork(get()) }
}

private fun getNavigatorHolder(cicerone : Cicerone<Router>) : NavigatorHolder = cicerone.navigatorHolder

private fun getRouter(cicerone : Cicerone<Router>) : Router = cicerone.router

private fun getPreference(context: Context) : SharedPreferences{
    return context.getSharedPreferences(USER, MODE_PRIVATE)
}