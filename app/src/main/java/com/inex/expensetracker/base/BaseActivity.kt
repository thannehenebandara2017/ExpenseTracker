package com.inex.expensetracker.base

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inex.expensetracker.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun showMessage(message: String = getString(R.string.oops_something_went_wrong)) {
        val alert = AlertDialog.Builder(this)
        alert.setMessage(message)
        alert.show()
    }

    fun getPixelsFromDp(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

}