package com.inex.expensetracker.views.dashboard

import com.inex.expensetracker.AppApplication
import com.inex.expensetracker.repository.AccountRepository
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DashboardViewModelTest {

    @Mock
    lateinit var context: AppApplication
    @Mock
    lateinit var accountRepo : AccountRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = AppApplication()
        accountRepo = AccountRepository(context)
    }

    @Test
    fun getAllAccountsByAscendingOrder() {
        accountRepo.getAllASC()
    }

    @After
    fun tearDown() {
    }
}