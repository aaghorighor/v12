package com.suftnet.v12.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.ui.*
import kotlinx.android.synthetic.main.seller_home_fragment.view.*

class SellerHomeFragment : Fragment() {

    private val USER_ID : String = "userId"
    private var userId: String? = null
    private lateinit var store: Store
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)
        }

        store = Store(context.requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.seller_home_fragment, container, false)
        init(view)
        return view
    }

    private fun init(view :View)
    {
        listener(view)
    }

    private fun listener(view :View)
    {
        //view.username.text = store.user!!.userName

        view.pending_action.setOnClickListener {
            val i = Intent(it!!.context, SellerPendingOrderActivity::class.java)
            startActivity(i)
        }

        view.completed_action.setOnClickListener {
            val i = Intent(it!!.context, SellerCompletedOrderActivity::class.java)
            startActivity(i)
        }

        view.exit.setOnClickListener {

            store.user = null;
            val intent = Intent(it!!.context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String) =
            SellerHomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                    }
                }
    }

}