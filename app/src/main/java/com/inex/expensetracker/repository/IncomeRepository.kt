package com.inex.expensetracker.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.inex.expensetracker.data.local.appdatabase.AppDatabase
import com.inex.expensetracker.data.local.dao.IncomeCatDataDao
import com.inex.expensetracker.data.local.entity.IncomeCatData

class IncomeRepository(applicationContext: Application) {
    private var incomeCatDataDao: IncomeCatDataDao

    companion object {
        @Volatile
        private var INSTANCE: IncomeRepository? = null

        fun getInstance(applicationContext: Application): IncomeRepository {
            return INSTANCE ?: IncomeRepository(applicationContext)
        }
    }

    init{
        val database: AppDatabase? = AppDatabase.getInstance(applicationContext.applicationContext)
        incomeCatDataDao = database!!.getIncomeCatDataDao()
    }

    fun insert(entity: IncomeCatData) {
        AsyncTask.execute {
            incomeCatDataDao.insert(entity)
        }
    }

    fun delete(entity: IncomeCatData) {
        AsyncTask.execute {
            incomeCatDataDao.delete(entity)
        }
    }

    fun update(entity: IncomeCatData) {
        AsyncTask.execute {
            incomeCatDataDao.update(entity)
        }
    }

    fun getAll(): LiveData<List<IncomeCatData>>{
        return incomeCatDataDao.getAll()
    }
}