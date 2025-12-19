package com.aman.urlbrowser.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aman.urlbrowser.databinding.FragmentHistoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.rvHistory.adapter = historyAdapter
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivClear.setOnClickListener {
            showClearConfirmationDialog()
        }

        binding.ivUpload.setOnClickListener {
            viewModel.uploadHistory()
        }
    }

    private fun observeViewModel() {
        viewModel.allUrls.observe(viewLifecycleOwner) { historyList ->
            if (historyList.isEmpty()) {
                binding.rvHistory.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.rvHistory.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
                historyAdapter.submitList(historyList)
            }
        }

        viewModel.uploadStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UploadStatus.Loading -> {
                    showSnackbar("Uploading history...")
                }

                is UploadStatus.Success -> {
                    showSnackbar(status.message)
                    viewModel.resetUploadStatus()
                }

                is UploadStatus.Error -> {
                    showSnackbar(status.message)
                    viewModel.resetUploadStatus()
                }

                is UploadStatus.Idle -> {
                    // Do nothing
                }
            }
        }

        viewModel.clearStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                showSnackbar("History cleared successfully")
            } else {
                showSnackbar("Failed to clear history")
            }
        }
    }

    private fun showClearConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Clear History")
            .setMessage("Are you sure you want to delete all browsing history?")
            .setPositiveButton("Clear") { _, _ ->
                viewModel.clearAllHistory()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
