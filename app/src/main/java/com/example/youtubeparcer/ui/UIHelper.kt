package com.example.youtubeparcer.ui

import android.content.Context
import android.widget.Toast

class UIHelper {
    fun showToast(context: Context, messange: String){
        Toast.makeText(context, messange, Toast.LENGTH_LONG).show()
    }
}