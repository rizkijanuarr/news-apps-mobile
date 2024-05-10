package com.santrikoding.newsapp.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.santrikoding.newsapp.R
import com.santrikoding.newsapp.data.Resource
import com.santrikoding.newsapp.databinding.FragmentDetailArticleBinding
import com.santrikoding.newsapp.utils.closeLoading
import com.santrikoding.newsapp.utils.showLoading
import com.santrikoding.newsapp.utils.withDateFormat
import com.santrikoding.newsapp.viewmodel.ViewModelFactory


class DetailArticleFragment : Fragment() {
    private var _binding: FragmentDetailArticleBinding? = null
    private val binding get() = _binding!!
    private val args: DetailArticleFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var isBookmark: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        setObserver()
    }


    private fun setObserver() {
        viewModel.getDetailArticle(args.article.slug).observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Loading -> {
                    showLoading(requireContext())

                }
                is Resource.Success -> {
                    closeLoading()
                    val article = resources.data
                    binding.imageArticle.load(article.image) {
                        transformations(RoundedCornersTransformation(12f))
                    }
                    binding.tvTitle.text = article.title
                    binding.tvCategory.text = article.category.name
                    binding.tvAuthor.text = article.user.name
                    binding.tvDate.text = article.createdAt.withDateFormat()
                    binding.tvView.text = article.viewsCount
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.wvContent.text =
                            Html.fromHtml(article.content, Html.FROM_HTML_MODE_LEGACY)
                    }
                }
                is Resource.Error -> {
                    closeLoading()
                }

                else -> {}
            }

        }

    }

    private fun setUi() = with(binding) {
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            inflateMenu(R.menu.menu_article)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.bookmark -> {
                        isBookmark = !isBookmark
                        changeIcon()
                        viewModel.setBookmark(args.article)
                        true
                    }
                    R.id.share -> {
                        val sendIntent = Intent(Intent.ACTION_SEND)
                        sendIntent.type = "text/plain"
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, args.article.title)
                        sendIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "https://news-api.appdev.my.id/" + args.article.slug
                        )

                        startActivity(Intent.createChooser(sendIntent, "Bagikan Artikel"))
                        true
                    }
                    else -> false
                }
            }
        }
        isBookmark = args.article.isBookmark
        changeIcon()
    }

    //Change Icon
    private fun changeIcon() {
        if (isBookmark) {
            binding.toolbar.menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmarked)
        } else {
            binding.toolbar.menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}