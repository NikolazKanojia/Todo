package com.project.todo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.project.todo.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {
  lateinit var binding: FragmentForgetPasswordBinding
    private lateinit var sharedPref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        sharedPref = requireContext()
            .getSharedPreferences("UserData", Context.MODE_PRIVATE)

        setOnClickListener()
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnResetPassword.setOnClickListener {
            resetPassword()
        }

        binding.tvBackToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun resetPassword() {

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword =
            binding.etConfirmPassword.text.toString().trim()

        val savedEmail = sharedPref.getString("EMAIL", "")

        when {

            email.isEmpty() -> {
                binding.etEmail.error = "Enter email"
            }

            password.isEmpty() -> {
                binding.etPassword.error = "Enter password"
            }

            confirmPassword.isEmpty() -> {
                binding.etConfirmPassword.error =
                    "Enter confirm password"
            }

            password != confirmPassword -> {

                Toast.makeText(
                    requireContext(),
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
            }

            email != savedEmail -> {

                Toast.makeText(
                    requireContext(),
                    "Email not found",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {

                sharedPref.edit()
                    .putString("PASSWORD", password)
                    .apply()

                Toast.makeText(
                    requireContext(),
                    "Password Updated Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().popBackStack()
            }
        }
    }


}


