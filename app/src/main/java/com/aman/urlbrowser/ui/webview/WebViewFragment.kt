package com.aman.urlbrowser.ui.webview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aman.urlbrowser.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WebViewViewModel by viewModels()
    private var initialUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialUrl = arguments?.getString("url") ?: ""

        setupWebView()
        setupClickListeners()
        observeViewModel()

        if (initialUrl.isNotEmpty()) {
            binding.webView.loadUrl(initialUrl)
            viewModel.updateCurrentUrl(initialUrl)
        }
    }

    private fun setupWebView() {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = false
                displayZoomControls = false

                cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                databaseEnabled = true
                allowFileAccess = true
                allowContentAccess = true
            }

            setLayerType(View.LAYER_TYPE_HARDWARE, null)

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    url?.let { viewModel.updateCurrentUrl(it) }
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    url?.let { viewModel.updateCurrentUrl(it) }
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    viewModel.updateLoadingProgress(newProgress)
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            val currentUrl = binding.webView.url ?: initialUrl
            viewModel.onBackButtonClicked(currentUrl)
        }

        binding.ivClose.setOnClickListener {
            viewModel.onCloseButtonClicked()
        }
    }

    private fun observeViewModel() {
        viewModel.currentUrl.observe(viewLifecycleOwner) { url ->
            binding.tvCurrentUrl.text = url
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.loadingProgress.observe(viewLifecycleOwner) { progress ->
            binding.progressBar.progress = progress
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) { navigationType ->
            navigationType?.let {
                when (it) {
                    is NavigationType.Back -> {
                        // Retain URL - pass back to HomeFragment
                        setFragmentResult("url_result", bundleOf("url" to it.retainUrl))
                        findNavController().navigateUp()
                    }

                    is NavigationType.Close -> {
                        // Clear URL - don't pass anything
                        setFragmentResult("url_result", bundleOf("url" to ""))
                        findNavController().navigateUp()
                    }
                }
                viewModel.onNavigationComplete()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.destroy()
        _binding = null
    }
}
