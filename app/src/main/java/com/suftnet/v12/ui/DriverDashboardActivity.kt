package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.ui.fragment.DriverHomeFragment
import kotlinx.android.synthetic.main.include_buyer_bottom_navigation.*
import kotlinx.android.synthetic.main.include_driver_bottom_navigation.*
import kotlinx.android.synthetic.main.include_driver_bottom_navigation.action_home
import kotlinx.android.synthetic.main.include_driver_bottom_navigation.action_messages
import kotlinx.android.synthetic.main.include_driver_bottom_navigation.action_profile

class DriverDashboardActivity : BaseAppCompatActivity() , View.OnClickListener  {
    private lateinit var store: Store

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.driver_dashboard)
        store = Store(this)
        init()
    }

    private fun init()
    {
        if(store.user != null)
        {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                    DriverHomeFragment.newInstance(store.user!!.id)).commit()
        }

        listener()
    }

    private fun listener()
    {
        action_home.setOnClickListener(this)
        action_job.setOnClickListener(this)
        action_messages.setOnClickListener(this)
        action_profile.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.action_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        DriverHomeFragment.newInstance(store.user!!.id)).commit()
            }
            R.id.action_job -> {
                val i = Intent(this, PendingJobsActivity::class.java)
                startActivity(i)
            }
            R.id.action_messages -> {
                val i = Intent(this, QuestionActivity::class.java)
                i.putExtra("from", "2")
                startActivity(i)
            }
            R.id.action_profile -> {
                val i = Intent(this, EditDriverActivity::class.java)
                i.putExtra("id", store.user!!.id)
                startActivity(i)
            }
        }
    }
}