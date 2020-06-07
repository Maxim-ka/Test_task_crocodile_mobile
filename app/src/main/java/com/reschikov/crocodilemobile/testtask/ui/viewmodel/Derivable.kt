package com.reschikov.crocodilemobile.testtask.ui.viewmodel

import okhttp3.ResponseBody

interface Derivable {
    suspend fun getResponse(url : String) : String?
}