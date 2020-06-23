package com.inex.expensetracker.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.inex.expensetracker.data.local.appdatabase.AppDatabase
import com.inex.expensetracker.data.local.dao.AccountDataDao
import com.inex.expensetracker.data.local.dao.ExpenseCatDataDao
import com.inex.expensetracker.data.local.dao.IncomeCatDataDao
import com.inex.expensetracker.data.local.dao.TransactionsDataDao
import com.inex.expensetracker.data.local.entity.AccountsData
import com.inex.expensetracker.data.local.entity.ExpenseCatData
import com.inex.expensetracker.data.local.entity.IncomeCatData
import com.inex.expensetracker.data.local.entity.TransactionsData

class TransactionRepository(applicationContext: Application) {
    private  var transactionsDataDao: TransactionsDataDao
    private  var accountDataDao: AccountDataDao
    private  var incomeCatDataDao: IncomeCatDataDao
    private  var expenseCatDataDao: ExpenseCatDataDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionRepository? = null

        fun getInstance(applicationContext: Application): TransactionRepository {
            return INSTANCE ?: TransactionRepository(applicationContext)
        }
    }

    init {
        val database: AppDatabase? = AppDatabase.getInstance(applicationContext.applicationContext)
        transactionsDataDao = database!!.getTransactionsDataDao()
        accountDataDao = database.getAccountsDataDao()
        incomeCatDataDao = database.getIncomeCatDataDao()
        expenseCatDataDao = database.getExpenseCatDataDao()
    }

    fun insert(entity: TransactionsData) {
        AsyncTask.execute {
            transactionsDataDao.insert(entity)
        }
    }

    fun getAll(): LiveData<List<TransactionsData>> {
        return transactionsDataDao.getAll()
    }

    fun getAllByAccountId(accId: Int): LiveData<List<TransactionsData>> {
        return transactionsDataDao.getAllByAccountId(accId)
    }

    fun getBalance(accId: Int): LiveData<Double> {
        return transactionsDataDao.getBalance(accId)
    }

    fun delete(item: TransactionsData) {
        AsyncTask.execute {
            transactionsDataDao.delete(item)
        }
    }

    fun updateAccountType(item: AccountsData) {
        AsyncTask.execute {
            accountDataDao.update(item)
        }
    }

    fun updateIncomeCatType(item: IncomeCatData?) {
        AsyncTask.execute {
            incomeCatDataDao.update(item)
        }
    }

    fun updateExpenseCatType(item: ExpenseCatData?) {
        AsyncTask.execute {
            expenseCatDataDao.update(item)
        }
    }
}