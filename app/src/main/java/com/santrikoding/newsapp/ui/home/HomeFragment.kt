package com.santrikoding.newsapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.santrikoding.newsapp.R
import com.santrikoding.newsapp.data.local.entity.ArticleEntity
import com.santrikoding.newsapp.data.remote.model.Category
import com.santrikoding.newsapp.databinding.FragmentHomeBinding
import com.santrikoding.newsapp.ui.search.SearchFragmentDirections
import com.santrikoding.newsapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Deklarasi kelas HomeFragment sebagai subclass dari Fragment
class HomeFragment : Fragment() {

    private var isDarkMode: Boolean = false

    // Deklarasi variabel binding sebagai nullable FragmentHomeBinding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi viewModel dengan viewModels
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    // Inisialisasi sliderAdapter dengan Lazy
    private val sliderAdapter by lazy {
        SliderAdapter()
    }


    // Inisialisasi categoryAdapter dengan Lazy
    private val categoryAdapter by lazy {
        CategoryAdapter { category ->
            detailCategory(category)
        }
    }

    private fun detailCategory(category: Category) {
        val action = HomeFragmentDirections.actionNavigationHomeToDetailCategoryFragment(
            slug = category.slug,
            title = category.name
        )
        findNavController().navigate(action)
    }

    // Inisialisasi articleAdapter dengan Lazy
    private val articleAdapter by lazy {
        ArticleAdapter({ article ->
            detailArticle(article)
        }, { article, position ->
            bookmarked(article, position)
        })
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

    private fun detailArticle(article: ArticleEntity) {
        val action = HomeFragmentDirections.actionNavigationHomeToDetailArticleFragment(article)
        findNavController().navigate(action)
    }


    // Method onCreateView dipanggil ketika Fragment pertama kali dibuat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout FragmentHomeBinding dan set ke _binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Return root dari _binding
        return binding.root
    }

    // Method onViewCreated dipanggil setelah Fragment dibuat dan tampilan dibuat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Panggil method setUi dan setObserver
        setUi()
        setObserver()
        setOnClick()
    }

    private fun setOnClick() = with(binding) {
        btnDarkMode.setOnClickListener {
            viewModel.saveThemeSetting(!isDarkMode)
        }
        btnSearch.setOnClickListener {
        }
    }



    // Method setObserver untuk mengambil data dari viewModel
    private fun setObserver() {
        // Memantau LiveData slider dari viewModel dan mengirimkan data ke sliderAdapter
        viewModel.slider.observe(viewLifecycleOwner) {
            sliderAdapter.submitList(it)
        }

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            isDarkMode = when (isDarkModeActive) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.btnDarkMode.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_light_mode)
                    )
                    true
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.btnDarkMode.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_dark_mode)
                    )
                    false
                }
            }
        }

        // Memantau Flow category dari viewModel dan mengirimkan data ke categoryAdapter dengan collectLatest
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.category.collectLatest {
                categoryAdapter.submitData(it)
            }
        }

        // Memantau Flow article dari viewModel dan mengirimkan data ke articleAdapter dengan collectLatest
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.article.collectLatest {
                articleAdapter.submitData(it)
            }
        }
    }

    // Method setUi untuk menampilkan tampilan pertama kali
    private fun setUi() = with(binding) {
        // Set adapter sliderAdapter ke ViewPager vpBanner
        vpBanner.adapter = sliderAdapter
        // Set adapter categoryAdapter ke RecyclerView rvCategory, dengan layout manager horizontal
        rvCategory.apply {
            adapter = categoryAdapter
            setHasFixedSize(false)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        // Set adapter articleAdpter ke RecyclerView rvArtcle, dengan layout manager Linear
        rvArticle.apply {
            adapter = articleAdapter
            setHasFixedSize(false)
            layoutManager =
                LinearLayoutManager(requireContext())
        }
    }

    // Method onDestroyView dipanggil ketika tampilan Fragment dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        // Set _binding menjadi null
        _binding = null
    }
}