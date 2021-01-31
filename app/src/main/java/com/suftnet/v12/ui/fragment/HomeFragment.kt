package com.suftnet.v12.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.ui.LoginActivity
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : Fragment() {

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
        var view = inflater.inflate(R.layout.home_fragment, container, false)
        init(view)
        return view
    }

    private fun init(view :View)
    {
        listener(view)
    }

    private fun listener(view :View)
    {
        view.username.text = store.user!!.userName

        view.exit.setOnClickListener {
            val intent = Intent(it!!.context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                    }
                }
    }

}