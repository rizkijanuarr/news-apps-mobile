package com.santrikoding.newsapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.santrikoding.newsapp.data.remote.model.Category
import com.santrikoding.newsapp.databinding.FragmentCategoryBinding
import com.santrikoding.newsapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*
* Deklarasi kelas CategoryFragment sebagai subclass dari Fragment
* */
class CategoryFragment : Fragment() {

    // Deklarasi variabel binding sebagai nullable FragmentCategoryBinding
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi viewModel dengan viewModels
    private val viewModel by viewModels<CategoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    // Inisialisasi categoryAllAdapter dengan Lazy
    private val categoryAllAdapter by lazy {
        CategoryAllAdapter { category ->
            detailCategory(category)
        }
    }

    private fun detailCategory(category: Category) {
        val action = CategoryFragmentDirections.actionNavigationCategoriesToDetailCategoryFragment(
            slug = category.slug,
            title = category.name
        )
        findNavController().navigate(action)
    }

    /*
    * Method onCreateView dipanggil ketika Fragment pertama kali dibuat
    * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout FragmentHomeBinding dan set ke _binding
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        // Return root dari _binding
        return binding.root
    }

    /*
    * Method onViewCreated dipanggil setelah Fragment dibuat dan tampilan dibuat
    * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Panggil method setUi dan setObserver
        setUi()
        setObserver()
    }

    /*
    * Method setObserver untuk mengambil data dari viewModel
    * */
    private fun setObserver() {
        // Memantau Flow category dari viewModel dan mengirimkan data ke categoryAllAdapter dengan collectLatest
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getCategory.collectLatest {
                categoryAllAdapter.submitData(it)
            }
        }
    }

    /*
    * Method setUi untuk menampilkan tampilan pertama kali
    * */
    private fun setUi() = with(binding) {
        // Set adapter categoryAllAdapter ke RecyclerView rvCategory, dengan layout manager horizontal
        rvCategory.apply {
            adapter = categoryAllAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

    }

    /*
    *  Method onDestroyView dipanggil ketika tampilan Fragment dihancurkan
    * */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}