package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.ui.fragment.HomeFragment
import kotlinx.android.synthetic.main.include_bottom_navigation.*
import kotlinx.android.synthetic.main.seller_dashboard.*

class SellerDashboardActivity : BaseAppCompatActivity() , View.OnClickListener  {
    private lateinit var store: Store

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_dashboard)
        store = Store(this)
        init()
    }

    private fun init()
    {
        if(store.user != null)
        {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                HomeFragment.newInstance(store.user!!.id)).commit()
        }

        listener()
    }

    private fun listener()
    {
        action_home.setOnClickListener(this)
        action_produce.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.action_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        HomeFragment.newInstance(store.user!!.id)).commit()
            }
            R.id.action_produce -> {
                val intent = Intent(this, ProduceActivity::class.java)
                startActivity(intent)
            }
        }
    }
}