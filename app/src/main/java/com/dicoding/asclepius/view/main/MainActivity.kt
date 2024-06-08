package com.dicoding.asclepius.view.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.article.ArticleActivity
import com.dicoding.asclepius.view.history.HistoryActivity
import com.dicoding.asclepius.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private lateinit var outputUri: Uri
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var dataClassifications: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)

        val intentResult = Intent(this, ResultActivity::class.java)

        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.analyzeButton.setOnClickListener {
            if (currentImageUri == null) {
                showToast(resources.getString(R.string.empty_image_warning))
            } else {
                analyzeImage(intentResult)
                moveToResult(intentResult)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history -> {
                val intentHistory = Intent(this, HistoryActivity::class.java)
                startActivity(intentHistory)
            }

            R.id.action_article -> {
                val intentArticle = Intent(this, ArticleActivity::class.java)
                startActivity(intentArticle)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val currentTime = System.currentTimeMillis()
            val filename = "croppedImage_$currentTime.jpg"

            outputUri = File(filesDir, filename).toUri()

            cropImage.launch(listOf(uri, outputUri))

        } else {
            showToast(resources.getString(R.string.empty_image_warning))
        }
    }

    private fun showImage(image: Uri) {
        binding.previewImageView.setImageURI(image)
    }


    private fun analyzeImage(intentResult: Intent) {
        imageClassifierHelper = ImageClassifierHelper(context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        showToast(error)
                    }
                }


                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        showLoading(true)
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val sortedCategories =
                                    it[0].categories.sortedByDescending { it?.score }

                                dataClassifications = sortedCategories.joinToString("\n") {
                                    "${it.label} " + NumberFormat.getPercentInstance()
                                        .format(it.score).trim()
                                }
                                showLoading(false)
                            } else {
                                showToast(resources.getString(R.string.image_is_empty))
                            }
                        }

                    }

                }
            }
        )
        currentImageUri?.let { imageClassifierHelper.classifyStaticImage(it) }
        intentResult.putExtra(URI_IMAGE, currentImageUri.toString())
        intentResult.putExtra(DATA_CLASSIFICATION, dataClassifications)
    }

    private fun moveToResult(intentResult: Intent) {
        startActivity(intentResult)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(value: Boolean) {
        with(binding) {
            if (value) {
                progressIndicator.visibility = VISIBLE
            } else {
                progressIndicator.visibility = INVISIBLE
            }
        }
    }

    private val uCropContract = object : ActivityResultContract<List<Uri>, Uri>() {
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(0f, 0f)
                .withMaxResultSize(2000, 2000)

            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }
    }

    private val cropImage = registerForActivityResult(uCropContract) { uri ->
        currentImageUri = uri
        showImage(uri)
    }

    companion object {
        const val URI_IMAGE = "uri_image"
        const val DATA_CLASSIFICATION = "data_classification"
    }
}

