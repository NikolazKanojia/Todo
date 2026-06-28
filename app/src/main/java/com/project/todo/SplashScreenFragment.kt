package com.project.todo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.project.todo.databinding.FragmentSplashScreenBinding


class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSplashScreenBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({

            if (!isAdded) return@postDelayed

            val sharedPreferences =
                requireContext().getSharedPreferences(
                    "UserData",
                    android.content.Context.MODE_PRIVATE
                )

            val isLoggedIn =
                sharedPreferences.getBoolean("IS_LOGGED_IN", false)

            if (isLoggedIn) {
                findNavController().navigate(
                    R.id.homeFragment,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreenFragment, true)
                        .build()
                )
            } else {
                findNavController().navigate(
                    R.id.loginFragment,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.splashScreenFragment, true)
                        .build()
                )
            }

        }, 5000)
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

}