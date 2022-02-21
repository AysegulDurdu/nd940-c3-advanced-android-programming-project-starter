package com.udacity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val bundle = intent.extras
        bundle?.let {
            textViewFileNameVal.text = it.getString(dwldTitle)
            textViewStatusVal.text = if(it.getBoolean(dwldStatus))  "Success" else "Failed"
        }

        buttonOk.setOnClickListener {
            onBackPressed()
        }

        fab.setOnClickListener{
            onBackPressed()
        }
    }

    companion object{
        const val dwldTitle = "downloadTitle"
        const val dwldStatus = "downloadStatus"

        fun getIntent(context : Context, title : String, status : Boolean) : Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(dwldTitle, title)
            intent.putExtra(dwldStatus, status)
            return intent
        }
    }

}
