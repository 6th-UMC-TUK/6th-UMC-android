package com.example.flo_clone.Function

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBottomSheetBinding
    private var onDismissListener: (() -> Unit)? = null

    fun setOnDismissListener(listener: () -> Unit) {
        onDismissListener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        setStyle(STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme)
        return binding.root
    }

    // BottomSheetFragment 가 사라졌을 때 MiniPlayer 보이기
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? MainActivity)?.showMiniPlayer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetPlayIv.setOnClickListener {
            Toast.makeText(requireActivity(), "듣기 버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        binding.bottomSheetPlaylistIv.setOnClickListener {
            Toast.makeText(requireActivity(), "재생목록 버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        binding.bottomSheetMylistIv.setOnClickListener {
            Toast.makeText(requireActivity(), "내 리스트 버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        binding.bottomSheetDeleteIv.setOnClickListener {
            Toast.makeText(requireActivity(), "삭제 버튼 클릭", Toast.LENGTH_SHORT).show()
        }
    }
}