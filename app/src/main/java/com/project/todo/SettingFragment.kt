package com.project.todo

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.todo.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        sharedPreferences =
            requireContext().getSharedPreferences("UserData", android.content.Context.MODE_PRIVATE)
        setonclick()
        return binding.root
    }

    private fun setonclick() {
        binding.layoutProfile.root.setOnClickListener {
            showProfileDialog()
        }

        binding.layoutNotifications.root.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Notification Settings",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.layoutTheme.root.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Theme selection coming soon",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.layoutPrivacy.root.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("About TaskFlow")
                .setMessage(
                    """
TaskFlow is a modern to-do application designed to help you organize your daily activities efficiently.

Features:
• Add & manage tasks
• Search tasks
• Star important tasks
• Task reminders
• Secure authentication
• Offline support with Room Database

Version: 1.0.0

Developed with ❤️ using Kotlin and Android.
        """.trimIndent()
                )
                .setPositiveButton("Got it", null)
                .show()
        }
        binding.layoutRateApp.root.setOnClickListener {

            val uri =
                Uri.parse("market://details?id=${requireContext().packageName}")

            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
        binding.aboutApp.root.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("About TaskFlow")
                .setMessage(
                    """
TaskFlow

Version 1.0

Developed using Kotlin, MVVM and Room Database.

Designed to help you organize your daily tasks efficiently.
            """.trimIndent()
                )
                .setPositiveButton("OK", null)
                .show()
        }
        binding.btnLogout.setOnClickListener {

            logout()

        }
    }
    private fun showProfileDialog() {

        val name = sharedPreferences.getString("FULL_NAME", "User")
        val email = sharedPreferences.getString("EMAIL", "Not Available")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("👤 Profile")
            .setMessage(
                """
Hello, $name! 👋

━━━━━━━━━━━━━━━━━━

📛 Name
$name

📧 Email
$email

━━━━━━━━━━━━━━━━━━

Thank you for using TaskFlow. Stay productive and keep accomplishing your goals every day!
            """.trimIndent()
            )
            .setPositiveButton("Close", null)
            .show()
    }
    private fun logout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->

                sharedPreferences.edit()
                    .putBoolean("IS_LOGGED_IN", false)
                    .apply()

                findNavController().navigate(
                    R.id.action_settingFragment_to_loginFragment
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}