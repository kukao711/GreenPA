package com.thepyprogrammer.greenpass.ui.information

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.thepyprogrammer.greenpass.R
import com.thepyprogrammer.greenpass.databinding.ActivityInfromationBinding
import com.thepyprogrammer.greenpass.ui.main.MainActivity
import com.thepyprogrammer.greenpass.ui.main.MainViewModel


class InfromationActivity : AppCompatActivity() {
    companion object CompanionObject {
        private lateinit var activity_:MainActivity
        fun updateActivity(activity: MainActivity) {
            activity_ = activity
        }
    }
    private var viewModel: MainViewModel? = null
    private lateinit var binding: ActivityInfromationBinding
    private lateinit var textView: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infromation)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_infromation)
        viewModel = activity_.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        button = findViewById(R.id.button)

        textView = binding.textView
        binding.myViewModel = viewModel
        binding.lifecycleOwner = this

        button.setOnClickListener{
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.invalidateAll()
        Log.d("TAG", viewModel?.pName?.value.toString())
    }
}