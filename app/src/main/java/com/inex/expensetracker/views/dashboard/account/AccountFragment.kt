package com.inex.expensetracker.views.dashboard.account


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.inex.expensetracker.R
import com.inex.expensetracker.base.BaseFragment
import com.inex.expensetracker.data.local.entity.AccountsData
import com.inex.expensetracker.data.local.entity.TransactionsData
import com.inex.expensetracker.utils.Utils
import com.inex.expensetracker.utils.Utils.Companion.showMessageWithTwoButtons
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment() {
    private lateinit var viewModel: AccountTransactionViewModel
    private lateinit var transactionAdapter: AccountTransactionAdapter
    private var accountsData: AccountsData? = null

    companion object {
        fun newInstance(accountsData: AccountsData): AccountFragment {
            val frag =
                AccountFragment()
            frag.accountsData = accountsData
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(AccountTransactionViewModel::class.java)
        initUI()
        getData()
        getBalance()
    }

    private fun initUI() {
        initAdapter()
    }

    private fun onLongPressItem(item: TransactionsData) {
        showMessageWithTwoButtons(context,
            R.string.are_you_sure_you_want_to_delete_item,
            R.string.yes,
            R.string.no,
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                viewModel.delete(item)
            },
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
    }

    private fun initAdapter() {
        transactionAdapter =
            AccountTransactionAdapter(::onLongPressItem)
        rv_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }
    }

    private fun getData() {
        accountsData?.id?.let { accId ->
            viewModel.getAllByAccountId(accId).observe(viewLifecycleOwner, Observer { list ->
                transactionAdapter.setDataSet(list as ArrayList<TransactionsData>)
            })
        }
    }

    /**
     * get account current balance
     */
    private fun getBalance() {
        accountsData?.id?.let { accId ->
            viewModel.getBalance(accId).observe(viewLifecycleOwner, Observer { balance ->
                if (balance != null) {
                    txt_total_value.text = Utils.getFormattedCurrencyValue(balance)
                } else {
                    txt_total_value.text = Utils.getFormattedCurrencyValue(0.0)
                }
            })
        }
    }
}
