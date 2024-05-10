package com.santrikoding.newsapp.ui.search

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.santrikoding.newsapp.data.local.entity.ArticleEntity
import com.santrikoding.newsapp.databinding.FragmentSearchBinding
import com.santrikoding.newsapp.ui.home.ArticleAdapter
import com.santrikoding.newsapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val articleAdapter by lazy {
        ArticleAdapter({ article ->
            detailArticle(article)
        }, { article, position ->
            bookmarked(article, position)
        })
    }

    // SAYA SUDAH MENDEFINISIKAN DISINI //
    private fun detailArticle(article: ArticleEntity) {
        val action =
            SearchFragmentDirections.actionSearchFragmentToDetailArticleFragment(article)
        findNavController().navigate(action)
    }

    private fun bookmarked(article: ArticleEntity, position: Int) {
        val isBookmarked = article.isBookmark
        viewModel.setBookmark(article)
        articleAdapter.notifyItemChanged(position)
        if (!isBookmarked) {
            message("Article ${article.title} berhasil di simpan")
        } else {
            message("Article ${article.title} berhasil di hapus")
        }
    }

    private fun message(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        setOnClick()
    }

    private fun setUi() = with(binding) {
        rvArticle.apply {
            adapter = articleAdapter
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
        }

        articleAdapter.addLoadStateListener { loadState ->
            val isLoading = loadState.refresh is LoadState.Loading
            val isEmpty = loadState.refresh is LoadState.NotLoading && articleAdapter.itemCount == 0
            tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            progressCircular.visibility = if (isLoading) View.VISIBLE else View.GONE

        }

        edtSearch.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateRepoListFromInput()
                    true
                } else {
                    false
                }
            }

            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateRepoListFromInput()
                    true
                } else {
                    false
                }
            }

            doOnTextChanged { _, _, _, count ->
                if (count == 0) {
                    updateRepoListFromInput()
                }
            }
        }
        updateRepoListFromInput()

    }

    private fun search(title: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchArticle(title).collectLatest {
                articleAdapter.submitData(it)
            }
        }

    }

    private fun updateRepoListFromInput() {
        binding.edtSearch.text?.trim().let {
            search(it.toString())
        }
    }

    private fun setOnClick() = with(binding) {
        btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}