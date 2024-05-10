package com.santrikoding.newsapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.santrikoding.newsapp.R
import com.santrikoding.newsapp.data.Resource
import com.santrikoding.newsapp.data.local.entity.ArticleEntity
import com.santrikoding.newsapp.databinding.FragmentDetailCategoryBinding
import com.santrikoding.newsapp.viewmodel.ViewModelFactory

class DetailCategoryFragment : Fragment() {
    private var _binding: FragmentDetailCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val articleByCategoryAdapter by lazy {
        ArticleByCategoryAdapter({ article ->
            detailArticle(article)
        }, { article, position ->
            bookmarked(article, position)
        })
    }

    private fun detailArticle(article: ArticleEntity) {
        val action =
            DetailCategoryFragmentDirections.actionDetailCategoryFragmentToDetailArticleFragment(
                article
            )
        findNavController().navigate(action)
    }

    private fun bookmarked(article: ArticleEntity, position: Int) {
        val isBookmarked = article.isBookmark
        viewModel.setBookmark(article)
        articleByCategoryAdapter.notifyItemChanged(position)
        if (!isBookmarked) {
            message("Article ${article.title} berhasil di simpan")
        } else {
            message("Article ${article.title} berhasil di hapus")
        }
    }

    private fun message(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private val args: DetailCategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        setObserver()
    }

    private fun setUi() = with(binding) {
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            title = args.title
        }
        rvArticle.apply {
            adapter = articleByCategoryAdapter
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setObserver() {
        viewModel.getDetailCategory(args.slug).observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val listArticle = resources.data
                    when (listArticle.isNotEmpty()) {
                        true -> {
                            articleByCategoryAdapter.submitList(listArticle)
                        }
                        else -> {
                            binding.tvEmpty.apply {
                                visibility = View.VISIBLE
                                text = "Article Empty"
                            }
                        }
                    }

                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resources.error, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}