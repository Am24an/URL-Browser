package com.aman.urlbrowser.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aman.urlbrowser.R
import com.aman.urlbrowser.databinding.FragmentHomeBinding
import com.aman.urlbrowser.utils.ValidationResult
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var carouselAdapter: CarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCarousel()
        setupClickListeners()
        observeViewModel()
        handleWebViewResult()
    }

    private fun setupCarousel() {
        val images = listOf(
            R.drawable.carousel_1,
            R.drawable.carousel_2,
            R.drawable.carousel_3
        )

        carouselAdapter = CarouselAdapter(images)
        binding.viewPagerCarousel.adapter = carouselAdapter
        binding.dotsIndicator.attachTo(binding.viewPagerCarousel)
    }

    private fun setupClickListeners() {
        binding.btnOpen.setOnClickListener {
            val url = binding.etUrl.text.toString()
            viewModel.validateAndOpenUrl(url)
        }

        binding.etUrl.setOnEditorActionListener { _, _, _ ->
            val url = binding.etUrl.text.toString()
            viewModel.validateAndOpenUrl(url)
            true
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_history -> {
                    findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.urlValidationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ValidationResult.Success -> {
                    binding.tilUrl.error = null
                }

                is ValidationResult.Error -> {
                    binding.tilUrl.error = result.message
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.navigateToWebView.observe(viewLifecycleOwner) { url ->
            url?.let {
                // Simple Bundle approach - No Safe Args needed!
                val bundle = Bundle().apply {
                    putString("url", it)
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_webViewFragment,
                    bundle
                )
                viewModel.onWebViewNavigationComplete()
            }
        }
    }


    private fun handleWebViewResult() {
        parentFragmentManager.setFragmentResultListener(
            "url_result",
            viewLifecycleOwner
        ) { _, bundle ->
            val url = bundle.getString("url") ?: ""
            binding.etUrl.setText(url)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
