package com.dicoding.asclepius.view.article

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.ArticleAdapter
import com.dicoding.asclepius.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private val articleViewModel: ArticleViewModel by viewModels { ArticleViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvArticle.layoutManager = layoutManager


        val adapter = ArticleAdapter()

        articleViewModel.getArticle()

        articleViewModel.listArticle.observe(this) {
            adapter.submitList(it)
            binding.rvArticle.adapter = adapter
        }

    }
}