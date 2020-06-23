package com.inex.expensetracker.views.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.inex.expensetracker.data.local.entity.AccountsData
import com.inex.expensetracker.repository.AccountRepository

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    var accountList:List<AccountsData> = emptyList()

    private var accountRepository: AccountRepository = AccountRepository.getInstance(application)

    /**
     * Get all account list ascending order by Id (account id )
     */
    fun getAllASC(): LiveData<List<AccountsData>> {
        return accountRepository.getAllASC()
    }
}