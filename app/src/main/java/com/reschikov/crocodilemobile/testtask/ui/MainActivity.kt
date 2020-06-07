package com.reschikov.crocodilemobile.testtask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.reschikov.crocodilemobile.testtask.R
import com.reschikov.crocodilemobile.testtask.navigation.Screens
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : AppCompatActivity() {

    private val navigatorHolder by inject<NavigatorHolder>()
    private val navigator = SupportAppNavigator(this, R.id.frame_master)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstanceState ?: get<Router>().replaceScreen(Screens.LoadingScreen())
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }
}
