package com.santrikoding.newsapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.santrikoding.newsapp.databinding.FragmentHomeBinding
import com.santrikoding.newsapp.viewmodel.ViewModelFactory


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance()
    }

    private val sliderAdapter by lazy {
        SliderAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        setObserver()

    }

    private fun setObserver() {
        viewModel.slider.observe(viewLifecycleOwner) {
            sliderAdapter.submitList(it)
        }
    }

    private fun setUi() = with(binding) {
        vpBanner.adapter = sliderAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}