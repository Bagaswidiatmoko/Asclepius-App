package com.dicoding.asclepius.view.history

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityHistoryItemBinding

class HistoryItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryItemBinding
    private lateinit var imageData: Uri
    private lateinit var description: String
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        imageData = Uri.parse(intent.getStringExtra(HISTORY_IMAGE))
        description = intent.getStringExtra(HISTORY_DESCRIPTION).toString()
        date = intent.getStringExtra(HISTORY_DATE).toString()


        with(binding) {
            historyImage.setImageURI(imageData)
            historyDescription.text = description
            historyDate.text = date
        }
    }

    companion object {
        const val HISTORY_IMAGE = "history_image"
        const val HISTORY_DESCRIPTION = "history_description"
        const val HISTORY_DATE = "history_date"
    }
}
