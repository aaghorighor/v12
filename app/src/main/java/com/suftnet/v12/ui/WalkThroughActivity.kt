package com.suftnet.v12.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.suftnet.v12.R
import com.suftnet.v12.util.loadImageFromResources
import com.suftnet.v12.util.makeTransaprant
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.walk_through.view.*
import kotlinx.android.synthetic.main.walk_through_activity.*

class WalkThroughActivity : BaseAppCompatActivity() {

    internal var mHeading =
        arrayOf("All Important Health tips", "Meditation is usefull for health", "Jogging is good for health")
    internal var mSubHeading = arrayOf(
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry.This is simply text ",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry.This is simply text  ",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry.This is simply text  "
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.walk_through_activity)
        makeTransaprant()

        title=""
        setToolbar(toolbar)
        setViewPage()
        listener()
    }

    private fun setViewPage()
    {
        val adapter = WalkAdapter()
        viewpager.adapter = adapter
        dots.attachViewPager(viewpager)
        dots.setDotDrawable(R.drawable.bg_color_primary, R.drawable.bg_back_dot)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                heading.text = mHeading[position]
                subHeading.text = mSubHeading[position]
            }

        })
        heading.text = mHeading[0]
        subHeading.text = mSubHeading[0]
    }

    private fun listener()
    {
        get_started.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    internal inner class WalkAdapter : PagerAdapter() {

        private val mImg =
            intArrayOf(
                R.drawable.walk_1,
                R.drawable.walk_2,
                R.drawable.walk_3
            )

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context)
                .inflate(R.layout.walk_through, container, false)

            view.image_placeholder.loadImageFromResources(applicationContext,mImg[position])
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return mImg.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as View
        }
    }
}