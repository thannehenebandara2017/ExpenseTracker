package com.inex.expensetracker.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.inex.expensetracker.data.local.appdatabase.AppDatabase
import com.inex.expensetracker.data.local.dao.ExpenseCatDataDao
import com.inex.expensetracker.data.local.entity.ExpenseCatData

class ExpenseRepository(applicationContext: Application) {
    private var expenseCatDataDao: ExpenseCatDataDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseRepository? = null

        fun getInstance(applicationContext: Application): ExpenseRepository {
            return INSTANCE ?: ExpenseRepository(applicationContext)
        }
    }

    init{
        val database: AppDatabase? = AppDatabase.getInstance(applicationContext.applicationContext)
        expenseCatDataDao = database!!.getExpenseCatDataDao()
    }

    fun insert(entity: ExpenseCatData) {
        AsyncTask.execute {
            expenseCatDataDao.insert(entity)
        }
    }

    fun delete(entity: ExpenseCatData) {
        AsyncTask.execute {
            expenseCatDataDao.delete(entity)
        }
    }

    fun update(entity: ExpenseCatData) {
        AsyncTask.execute {
            expenseCatDataDao.update(entity)
        }
    }

    fun getAll(): LiveData<List<ExpenseCatData>>{
        return expenseCatDataDao.getAll()
    }
}