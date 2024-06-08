package com.dicoding.asclepius.view.result

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.DateHelper
import com.dicoding.asclepius.view.main.MainActivity


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageData: Uri
    private lateinit var dataClassifications: String
    private lateinit var date: String
    private val resultViewModel: ResultViewModel by viewModels { ResultViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageData = Uri.parse(intent.getStringExtra(MainActivity.URI_IMAGE))
        dataClassifications = intent.getStringExtra(MainActivity.DATA_CLASSIFICATION).toString()
        date = DateHelper.getCurrentDate()

        val history = History()

        history.let {
            history.avatarUri = imageData.toString()
            history.description = dataClassifications
            history.date = date
        }

        resultViewModel.insert(history)

        binding.resultImage.setImageURI(imageData)
        binding.resultText.text = dataClassifications
        binding.resultDate.text = date
    }


}