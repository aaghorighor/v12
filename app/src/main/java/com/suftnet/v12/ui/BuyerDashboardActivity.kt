package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.ui.fragment.BuyerHomeFragment
import kotlinx.android.synthetic.main.include_bottom_navigation.action_home
import kotlinx.android.synthetic.main.include_buyer_bottom_navigation.*

class BuyerDashboardActivity : BaseAppCompatActivity() , View.OnClickListener  {
    private lateinit var store: Store

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buyer_dashboard)
        store = Store(this)
        init()
    }

    private fun init()
    {
        if(store.user != null)
        {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    BuyerHomeFragment.newInstance(store.user!!.id)).commit()
        }

        listener()
    }

    private fun listener()
    {
        action_home.setOnClickListener(this)
        action_market.setOnClickListener(this)
        action_messages.setOnClickListener(this)
        action_profile.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.action_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        BuyerHomeFragment.newInstance(store.user!!.id)).commit()
            }
            R.id.action_market -> {
                val intent = Intent(this, MarketActivity::class.java)
                startActivity(intent)
            }
            R.id.action_messages -> {
                val i = Intent(this, QuestionActivity::class.java)
                i.putExtra("from", "1")
                startActivity(i)
            }
            R.id.action_profile -> {
                val i = Intent(this, EditBuyerActivity::class.java)
                i.putExtra("id", store.user!!.id)
                startActivity(i)
            }
        }
    }
}