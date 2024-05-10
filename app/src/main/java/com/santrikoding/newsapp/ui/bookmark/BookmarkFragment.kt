package com.santrikoding.newsapp.ui.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.santrikoding.newsapp.data.local.entity.ArticleEntity
import com.santrikoding.newsapp.databinding.FragmentBookmarkBinding
import com.santrikoding.newsapp.viewmodel.ViewModelFactory

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<BookmarkViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val bookmarkAdapter by lazy {
        BookmarkAdapter(
            { article ->
                detailArticle(article)
            },
            { article ->
                updateArticle(article)
            }
        )
    }

    private fun detailArticle(article: ArticleEntity) {
        val action =
            BookmarkFragmentDirections.actionNavigationBookmarkToDetailArticleFragment(article)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        setObserver()
    }

    private fun setObserver() = with(viewModel) {

        getBookmarkArticle().observe(viewLifecycleOwner) { listArticle ->
            val isVisible = if (listArticle.isEmpty()) View.VISIBLE else View.GONE
            binding.tvEmpty.visibility = isVisible
            bookmarkAdapter.submitList(listArticle)
        }

    }

    private fun setUi() = with(binding) {
        rvBookmark.apply {
            adapter = bookmarkAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun updateArticle(article: ArticleEntity) {
        viewModel.setBookmark(article)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}