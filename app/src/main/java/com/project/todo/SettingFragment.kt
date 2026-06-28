package com.project.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.project.todo.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSettingBinding.inflate(inflater,container,false)
        setonclick()
        return binding.root
    }

    private fun setonclick() {
        binding.btnLogout.setOnClickListener{
            val sharedPreferences =
                requireContext().getSharedPreferences("UserData", android.content.Context.MODE_PRIVATE)

            sharedPreferences.edit()
                .putBoolean("IS_LOGGED_IN", false)
                .apply()

            // Navigate to the login fragment
            findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
        }
//        binding.bottomNavigation.setOnItemSelectedListener { item ->
//
//            when(item.itemId) {
//
//                R.id.homeFragment -> {
//                    findNavController().navigate(R.id.homeFragment)
//
//                    true
//                }
//
//                R.id.statusFragment -> {
//                    findNavController().navigate(R.id.statusFragment)
//
//                    true
//                }
//
//                R.id.addTaskFragment -> {
//                    findNavController().navigate(R.id.addTaskFragment)
//
//                    true
//                }
//
//                R.id.settingFragment -> {
//                    findNavController().navigate(R.id.settingFragment)
//
//                    true
//                }
//
//                else -> false
//            }
//        }

    }


}