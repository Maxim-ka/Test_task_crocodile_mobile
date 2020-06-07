package com.reschikov.crocodilemobile.testtask.navigation

import com.reschikov.crocodilemobile.testtask.ui.fragments.LinkFragment
import com.reschikov.crocodilemobile.testtask.ui.fragments.LoadFragment
import com.reschikov.crocodilemobile.testtask.ui.fragments.TinderFragment
import com.reschikov.crocodilemobile.testtask.ui.fragments.WebFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

sealed class Screens : SupportAppScreen(){

    class LoadingScreen : Screens() {
        override fun getFragment() = LoadFragment()
    }

    class LinkScreen : Screens() {
        override fun getFragment() = LinkFragment()
    }

    class WebScreen : Screens(){
        override fun getFragment() = WebFragment()
    }

    class TinderScreen : Screens(){
        override fun getFragment() = TinderFragment()
    }
}