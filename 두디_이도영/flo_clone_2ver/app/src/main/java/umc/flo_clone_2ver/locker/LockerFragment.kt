package umc.flo_clone_2ver.locker

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import umc.flo_clone_2ver.adapter.ViewpagerFragmentAdapter
import umc.flo_clone_2ver.adapter.ViewpagerFragmentAdapter.Companion.LOCKER
import umc.flo_clone_2ver.databinding.FragmentLockerBinding
import umc.flo_clone_2ver.login.LoginActivity
import umc.flo_clone_2ver.presentation.MainActivity

class LockerFragment : Fragment() {
    private lateinit var binding: FragmentLockerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewPager2()
    }
    private fun initViewPager2() {
        val fragmentStateAdapter = ViewpagerFragmentAdapter(this, LOCKER)
        binding.fragmentLockerViewpager2.adapter = fragmentStateAdapter
        TabLayoutMediator(
            binding.fragmentLockerTablayout,
            binding.fragmentLockerViewpager2
        ) { tab, position ->
            binding.fragmentLockerViewpager2.currentItem = tab.position
            tab.text = when (position) {
                0 -> "저장한 곡"
                1 -> "음악파일"
                else -> "저장앨범"
            }
        }.attach()

        binding.fragmentLockerLoginTv.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun initViews(){
        val jwt: Int = getJwt()
        if(jwt == 0){
            binding.fragmentLockerLoginTv.text = "로그인"
            binding.fragmentLockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        else{
            binding.fragmentLockerLoginTv.text = "로그아웃"
            binding.fragmentLockerLoginTv.setOnClickListener {
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun logout(){
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()
    }
}