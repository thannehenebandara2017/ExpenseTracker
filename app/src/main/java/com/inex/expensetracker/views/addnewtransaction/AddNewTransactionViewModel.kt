package com.inex.expensetracker.views.addnewtransaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.inex.expensetracker.data.local.entity.AccountsData
import com.inex.expensetracker.data.local.entity.ExpenseCatData
import com.inex.expensetracker.data.local.entity.IncomeCatData
import com.inex.expensetracker.data.local.entity.TransactionsData
import com.inex.expensetracker.repository.TransactionRepository

class AddNewTransactionViewModel : AndroidViewModel {

    private var transactionRepository: TransactionRepository

    constructor(application: Application) : super(application) {
        transactionRepository = TransactionRepository.getInstance(application)
    }

    fun insert(entity: TransactionsData) {
        transactionRepository.insert(entity)
    }

    fun updateAccountType(entity: AccountsData) {
        transactionRepository.updateAccountType(entity)
    }

    fun updateIncomeCatType(entity: IncomeCatData) {
        transactionRepository.updateIncomeCatType(entity)
    }

    fun updateExpenseCatType(entity: ExpenseCatData) {
        transactionRepository.updateExpenseCatType(entity)
    }
}