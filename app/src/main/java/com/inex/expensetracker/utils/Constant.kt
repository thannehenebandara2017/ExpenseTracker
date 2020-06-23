package com.inex.expensetracker.utils

class Constant {
    companion object {
        const val DATE_AND_TIME_FORMAT: String = "MMMM dd yyyy, h:mm aa"
        const val TIME_FORMAT: String = "h:mm aa"
        const val DATE_FORMAT: String = "EEEE, MMMM d, h:mm aa"
        const val EXTRAS_ENABLE_ADD_NEW_ACCOUNT = "enable_add_new_account"
        const val EXTRAS_SELECTION_TYPE = "selection_type"
        const val EXTRAS_ACCOUNT_ITEM = "account_item"
        const val EXTRAS_INCOME_ITEM = "income_item"
        const val EXTRAS_EXPENSE_ITEM = "expense_item"

        const val REQUEST_ACCOUNT_LIST = 800
        const val REQUEST_INCOME_LIST = 801
        const val REQUEST_EXPENSE_LIST = 802
    }
}