package com.suftnet.v12.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.snackbar.Snackbar
import com.suftnet.v12.Adapter.QuestionAdapter
import com.suftnet.v12.R
import com.suftnet.v12.api.model.request.CreateQuestion
import com.suftnet.v12.api.model.response.Question
import com.suftnet.v12.model.Error
import com.suftnet.v12.util.Util
import com.suftnet.v12.viewModel.QuestionViewModel
import kotlinx.android.synthetic.main.description_dialog.*
import kotlinx.android.synthetic.main.question_placeholder.*
import org.jetbrains.anko.alert

@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
class QuestionActivity : BaseAppCompatActivity() {
    companion object {
        const val TAG = "QuestionActivity"
    }

    private lateinit var viewModel: QuestionViewModel
    private lateinit var questionAdapter : QuestionAdapter
    private lateinit var recyclerView : RecyclerView
    private val context : Context = this
    private var totalCount = 0.0
    private var snackBar: Snackbar? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_placeholder)

        init()
        handler(1)
    }

    private fun init()
    {
        setToolBar("Messages")
        viewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.layoutManager = LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);

        questionAdapter = QuestionAdapter(context,recyclerView, ArrayList<Question>())
        recyclerView.adapter = questionAdapter

        listener()
    }

    private fun setToolBar(title: String) {

        main_title.text =title
    }

    private fun listener()
    {
        back_action.setOnClickListener {

            var from = intent.getStringExtra("from")

            when(from)
            {
                "0"-> {
                    var i = Intent(this@QuestionActivity, SellerDashboardActivity::class.java)
                    startActivity(i)
                }

                "1"-> {
                    var i = Intent(this@QuestionActivity, BuyerDashboardActivity::class.java)
                    startActivity(i)
                }

                "2"-> {
                    var i = Intent(this@QuestionActivity, DriverDashboardActivity::class.java)
                    startActivity(i)
                }
            }

        }

        add_action.setOnClickListener {
            onDialog()
        }

        swipe_refresh.setOnRefreshListener(OnRefreshListener {
            swipe_refresh.isRefreshing = false
            questionAdapter.reset()
            handler(1)
        })

        questionAdapter.setOnItemClickListener(object:QuestionAdapter.OnItemClickListener {
            override fun onSms(question: Question, position: Int) {
                sms(question.phoneNumber)
            }
            override fun onPhone(question: Question, position: Int) {
                phone(question.phoneNumber)
            }
        })

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.listError.observe(
            this,
            Observer {
                onError(it)
            }
        )
    }

    private fun loadPage()
    {
        viewModel.fetch().observe(this, Observer {
                        if(!it.isNullOrEmpty())
            {
                add(it.toMutableList())
            }else {
                showEmptyLayout(true)
            }
        })
    }

    private fun add(questions: MutableList<Question>) {

        recyclerView.visibility = View.VISIBLE
        questionAdapter.add(questions)
        if (questions.size == 0) {
            showEmptyLayout(true)
        }
    }

    private fun add(question: Question) {
        recyclerView.visibility = View.VISIBLE
        questionAdapter.add(question)
    }

    private fun phone(phoneNumber: String) {
        if (!Util.isValidPhoneNumber(phoneNumber)) {
            toastShort("Phone Number not Invalid")
            return
        }
        val j = Intent(Intent.ACTION_DIAL)
        j.data = Uri.parse("tel:" + phoneNumber)
        startActivity(j)
    }

    private fun sms(phoneNumber: String) {
        if (!Util.isValidPhoneNumber(phoneNumber)) {
            toastShort("Phone Number not Invalid")
            return
        }
        val j = Intent(Intent.ACTION_SENDTO)
        j.data = Uri.parse("smsto:"  + phoneNumber);
        startActivity(j)
    }

    private fun onDialog() {

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.description_dialog)
        dialog.setCancelable(true)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { dialog.dismiss() }
        (dialog.findViewById<View>(R.id.bt_submit) as AppCompatButton).setOnClickListener {

            onSave( dialog.description.text.toString())
            dialog.dismiss()
        }

        dialog.show()
        dialog.window!!.attributes = lp

        dialog.description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                dialog.findViewById<View>(R.id.bt_submit).isEnabled = s.toString().trim().isNotEmpty();
            }
        })
    }

    private fun showEmptyLayout(show: Boolean) {
        val emptyLayout = findViewById<View>(R.id.lyt_no_item)
        if (show) {
            recyclerView.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
    }

    private fun showFailedView(message: String) {
        snackBar = Snackbar.make(this.parent_view, message, Snackbar.LENGTH_INDEFINITE)
        snackBar!!.setAction(getString(R.string.PleaseTryAgain), View.OnClickListener {
            handler(1)
            recyclerView.scrollToPosition(questionAdapter.getItemCount() - 1)
        })
        snackBar!!.show()
    }

    private fun hidErrorLayout() {
        if (snackBar != null && snackBar!!.isShownOrQueued()) snackBar!!.dismiss()
    }

    private fun handler(page_no: Int) {
        hidErrorLayout()
        showEmptyLayout(false)
        if (page_no == 1) {
        } else {
            questionAdapter.setLoading()
        }
        Handler().postDelayed(Runnable { loadPage() }, 10)
    }

    private fun onSave(description : String){
        var createQuestion = CreateQuestion(description)
        viewModel.create(createQuestion).observe(
            this,
            Observer {
                add(it)
            }
        )
    }

    private fun onError(it :Error)
    {
        alert {
            title = getString(R.string.ErrorMessages)
            message = """${it.messages}${{ it.statusCode }}"""
            isCancelable = false
            positiveButton(getString(R.string.Ok)) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }

}