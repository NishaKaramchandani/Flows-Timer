package com.example.baseproject.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentListBinding
import com.example.baseproject.utils.Utils
import com.example.baseproject.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.functions.Cancellable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentList : Fragment(R.layout.fragment_list), OnClickListener {

    private lateinit var binding: FragmentListBinding

    private val viewModel: ListViewModel by viewModels()

    private lateinit var job: Job

    companion object {
        private const val TAG = "FragmentList"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)

        binding.startButton.setOnClickListener(this)
        binding.addButton.setOnClickListener(this)
        binding.subtractButton.setOnClickListener(this)
        binding.stopButton.setOnClickListener(this)
        binding.resetButton.setOnClickListener(this)

        job = createCollectTimerJob()
    }

    private fun createCollectTimerJob() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.timerFlow.cancellable().collect {
                Log.d(TAG, "createCollectTimerJob: $it")
                binding.timerTextview.text = Utils.formatTime(it)
            }
       }
    }

    override fun onClick(view: View?) {
        view?.let {
            when (it.id) {
                R.id.start_button -> startTimer()
                R.id.add_button -> addTimer()
                R.id.subtract_button -> subtractButton()
                R.id.stop_button -> stopButton()
                R.id.reset_button -> resetButton()
                else -> {}
            }
        }
    }

    private fun startTimer() {
        if(::job.isInitialized.not() || job.isCancelled){
            job = createCollectTimerJob()

        }
        viewModel.initTimer(30)
    }

    private fun addTimer() {
        viewModel.addToTimer()
    }

    private fun subtractButton() {
        viewModel.subtractFromTimer()
    }

    private fun stopButton() {
        job.cancel()
        viewModel.initTimer(0)
    }

    private fun resetButton() {
        job.cancel()
        viewModel.initTimer(0)
        binding.timerTextview.text = resources.getText(R.string.default_time)
    }
}