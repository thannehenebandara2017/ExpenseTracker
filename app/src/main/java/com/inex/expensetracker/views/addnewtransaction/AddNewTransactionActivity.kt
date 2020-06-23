package com.inex.expensetracker.views.addnewtransaction

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.inex.expensetracker.R
import com.inex.expensetracker.base.BaseActivity
import com.inex.expensetracker.data.local.entity.AccountsData
import com.inex.expensetracker.data.local.entity.ExpenseCatData
import com.inex.expensetracker.data.local.entity.IncomeCatData
import com.inex.expensetracker.data.local.entity.TransactionsData
import com.inex.expensetracker.model.SelectionTypes
import com.inex.expensetracker.model.TransactionTypes
import com.inex.expensetracker.utils.Constant
import com.inex.expensetracker.utils.Constant.Companion.EXTRAS_ACCOUNT_ITEM
import com.inex.expensetracker.utils.Constant.Companion.EXTRAS_EXPENSE_ITEM
import com.inex.expensetracker.utils.Constant.Companion.EXTRAS_INCOME_ITEM
import com.inex.expensetracker.utils.Constant.Companion.REQUEST_ACCOUNT_LIST
import com.inex.expensetracker.utils.Constant.Companion.REQUEST_EXPENSE_LIST
import com.inex.expensetracker.utils.Constant.Companion.REQUEST_INCOME_LIST
import com.inex.expensetracker.utils.Utils
import com.inex.expensetracker.views.addnewtransaction.selectionlist.SelectionListActivity
import kotlinx.android.synthetic.main.activity_add_new_transaction.*
import java.util.*


class AddNewTransactionActivity : BaseActivity(), View.OnClickListener {

     lateinit var viewModel: AddNewTransactionViewModel
     var transactionType = TransactionTypes.EXPENSE.name
     var selectedAccount: AccountsData? = null

    companion object {
        fun getIntent(context: Context, accountData: AccountsData): Intent {
            val intent = Intent(context, AddNewTransactionActivity::class.java)
            intent.putExtra(Constant.EXTRAS_ACCOUNT_ITEM, accountData)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_transaction)
        viewModel = ViewModelProvider(this).get(AddNewTransactionViewModel::class.java)
        getExtras()
        initUI()
    }

    private fun initUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.title_add_transaction)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white)
        setTransactionType()
        edt_account.setText(selectedAccount?.name ?: "")
        edt_account.tag = selectedAccount
        edt_account.setOnClickListener(this)
        edt_category.setOnClickListener(this)
    }

    private fun getExtras() {
        if (intent.hasExtra(EXTRAS_ACCOUNT_ITEM)) {
            selectedAccount = intent.getParcelableExtra<AccountsData>(EXTRAS_ACCOUNT_ITEM)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done -> {
                if (validation()) {
                    var income: IncomeCatData? = null
                    var expense: ExpenseCatData? = null
                    when (transactionType) {
                        TransactionTypes.INCOME.name -> {
                            income = edt_category.tag as IncomeCatData
                        }
                        else -> {
                            expense = edt_category.tag as ExpenseCatData
                        }
                    }
                    saveTransaction(
                        transactionType,
                        edt_account.tag as AccountsData,
                        income,
                        expense ,
                        edt_amount.text.toString().toDouble()
                    )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * transaction save into Database
     */
    private fun saveTransaction(
        transactionType: String,
        accountModel: AccountsData,
        incomeCatData: IncomeCatData?,
        expenseCatData: ExpenseCatData?,
        amount: Double
    ) {
        val transaction = TransactionsData()
        transaction.accId = accountModel.id

        when (transactionType) {
            TransactionTypes.INCOME.name -> {
                transaction.catId = incomeCatData?.id
                transaction.catName = incomeCatData?.name
                transaction.isIncome = true
                transaction.amount = amount

                incomeCatData?.isActive = true
                incomeCatData?.let { viewModel.updateIncomeCatType(it) }
            }
            TransactionTypes.EXPENSE.name -> {
                if (edt_category.tag is ExpenseCatData) {
                    transaction.catId = expenseCatData?.id
                    transaction.isIncome = false
                    transaction.amount = -edt_amount.text.toString().toDouble()
                    transaction.catName = expenseCatData?.name
                    expenseCatData?.isActive = true
                    expenseCatData?.let { viewModel.updateExpenseCatType(it) }
                }
            }
        }
        transaction.currency = Currency.getInstance(Locale.getDefault()).currencyCode
        transaction.timestamp = System.currentTimeMillis()
        val id = viewModel.insert(transaction)


        accountModel.isActive = true
        viewModel.updateAccountType(accountModel)
        Utils.showMessage(
            this,
            getString(R.string.transaction_added_successful),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                onBackPressed()
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_transaction_menu, menu)
        return true
    }

    /**
     * set transaction types view
     */
    private fun setTransactionType() {
        val chipExpense = Chip(this)
        val chipIncome = Chip(this)
        Utils.customWeight.setMargins(getPixelsFromDp(this, 8f), 0, 0, 0)
        chipIncome.layoutParams = Utils.customWeight
        chipIncome.chipBackgroundColor = ContextCompat.getColorStateList(this, R.color.bg_chip)
        chipIncome.setTextColor(ContextCompat.getColorStateList(this, R.color.text_chip))
        chipIncome.text = getString(R.string.income)
        chipIncome.isFocusable = true
        chipIncome.isClickable = true
        chipIncome.textSize = 14.0f
        chipIncome.isCheckable = true
        chipIncome.checkedIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_check_circle_black_24dp)
        chipIncome.setOnClickListener {
            chipExpense.isChecked = false
            transactionType = TransactionTypes.INCOME.name
            resetFields()
            edt_category.hint = getString(R.string.select_income)
        }

        chipExpense.layoutParams = Utils.customWeight
        chipExpense.chipBackgroundColor = ContextCompat.getColorStateList(this, R.color.bg_chip)
        chipExpense.setTextColor(ContextCompat.getColorStateList(this, R.color.text_chip))
        chipExpense.text = getString(R.string.expense)
        chipExpense.isFocusable = true
        chipExpense.isClickable = true
        chipExpense.textSize = 14.0f
        chipExpense.isCheckable = true
        chipExpense.isChecked = true
        chipExpense.checkedIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_check_circle_black_24dp)
        chipExpense.setOnClickListener {
            chipIncome.isChecked = false
            transactionType = TransactionTypes.EXPENSE.name
            resetFields()
            edt_category.hint = getString(R.string.select_expense)
        }
        resetFields()
        chip_group_transaction_types.addView(chipExpense)
        chip_group_transaction_types.addView(chipIncome)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_ACCOUNT_LIST -> {
                    val item = data?.getParcelableExtra<AccountsData>(EXTRAS_ACCOUNT_ITEM)
                    edt_account.setText(item?.name)
                    edt_account.tag = item
                }
                REQUEST_INCOME_LIST -> {
                    val item = data?.getParcelableExtra<IncomeCatData>(EXTRAS_INCOME_ITEM)
                    edt_category.setText(item?.name)
                    edt_category.tag = item
                }
                REQUEST_EXPENSE_LIST -> {
                    val item = data?.getParcelableExtra<ExpenseCatData>(EXTRAS_EXPENSE_ITEM)
                    edt_category.setText(item?.name)
                    edt_category.tag = item
                }
            }
        }
    }

    /**
     * Reset fields when changing transaction type
     */
    private fun resetFields() {
        edt_category.hint = getString(R.string.select_expense)
        edt_amount.hint =
            "${getString(R.string.enter_amount)} (${Utils.getCurrencyInstance().currency})"
        edt_amount.text.clear()
        edt_category.text.clear()
        edt_category.tag = null
    }

    /**
     * Validate input fields
     */
    private fun validation(): Boolean {
        when {
            edt_account.tag == null -> {
                showMessage(getString(R.string.account_name_is_required))
                return false
            }
            edt_category.tag == null -> {
                if (transactionType == TransactionTypes.INCOME.name) {
                    showMessage(getString(R.string.income_is_required))
                } else {
                    showMessage(getString(R.string.expense_is_required))
                }
                return false
            }
            edt_amount.text.isNullOrEmpty() -> {
                showMessage(getString((R.string.amount_is_required)))
                return false
            }
            else -> return true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            edt_category.id -> {
                clickOnCategory()
            }
            edt_account.id -> {
                clickOnAccount()
            }
        }
    }

    /**
     * Open account type selection view
     */
    private fun clickOnAccount() {
        val intent = SelectionListActivity.getIntent(this, false, SelectionTypes.ACCOUNT.value)
        startActivityForResult(intent, REQUEST_ACCOUNT_LIST)
    }

    /**
     * Open transaction selection view
     */
    private fun clickOnCategory() {
        when (transactionType) {
            TransactionTypes.INCOME.name -> {
                val intent = SelectionListActivity.getIntent(this, SelectionTypes.INCOME.value)
                startActivityForResult(intent, REQUEST_INCOME_LIST)
            }
            TransactionTypes.EXPENSE.name -> {
                val intent = SelectionListActivity.getIntent(this, SelectionTypes.EXPENSE.value)
                startActivityForResult(intent, REQUEST_EXPENSE_LIST)
            }
        }
    }
}
