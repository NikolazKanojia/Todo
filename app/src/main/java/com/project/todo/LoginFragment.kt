package com.project.todo

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var ivPasswordToggle: ImageView
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvSignUp: TextView
    private lateinit var cardGoogle: CardView
    private lateinit var cardApple: CardView
    private lateinit var tvAppName: TextView

    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupAppNameSpan()
        setupClickListeners()
        animateViews()
    }

    private fun initViews(view: View) {
        etEmail           = view.findViewById(R.id.etEmail)
        etPassword        = view.findViewById(R.id.etPassword)
        ivPasswordToggle  = view.findViewById(R.id.ivPasswordToggle)
        btnLogin          = view.findViewById(R.id.btnLogin)
        tvForgotPassword  = view.findViewById(R.id.tvForgotPassword)
        tvSignUp          = view.findViewById(R.id.tvSignUp)
        cardGoogle        = view.findViewById(R.id.cardGoogle)
        cardApple         = view.findViewById(R.id.cardApple)
        tvAppName         = view.findViewById(R.id.tvAppName)
    }

    /**
     * Makes "Task" black and "Flow" purple using SpannableString
     */
    private fun setupAppNameSpan() {
        val fullText = "TaskFlow"
        val spannable = SpannableString(fullText)

        // "Task" → dark/black
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#1a1a2e")),
            0, 4,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // "Flow" → brand purple
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#667eea")),
            4, fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvAppName.text = spannable
    }

    private fun setupClickListeners() {

        // Password visibility toggle
        ivPasswordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivPasswordToggle.setColorFilter(Color.parseColor("#667eea"))
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivPasswordToggle.setColorFilter(Color.parseColor("#bbbccc"))
            }
            // Keep cursor at end
            etPassword.setSelection(etPassword.text.length)
        }

        // Login button
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                performLogin(email, password)
            }
        }

        // Forgot password
        tvForgotPassword.setOnClickListener {
            Toast.makeText(requireContext(), "Forgot Password tapped", Toast.LENGTH_SHORT).show()
            // Navigate using NavController (if using Navigation Component):
            // findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        // Google sign-in
        cardGoogle.setOnClickListener {
            Toast.makeText(requireContext(), "Continue with Google", Toast.LENGTH_SHORT).show()
            // Integrate Google Sign-In SDK here
        }

        // Apple sign-in
        cardApple.setOnClickListener {
            Toast.makeText(requireContext(), "Continue with Apple", Toast.LENGTH_SHORT).show()
            // Integrate Apple Sign-In here
        }

        // Sign Up
        tvSignUp.setOnClickListener {
            //Toast.makeText(requireContext(), "Navigate to Sign Up", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter a valid email"
            etEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return false
        }
        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            etPassword.requestFocus()
            return false
        }
        return true
    }

    private fun performLogin(email: String, password: String) {

        val sharedPreferences =
            requireContext().getSharedPreferences("UserData", android.content.Context.MODE_PRIVATE)

        val savedEmail = sharedPreferences.getString("EMAIL", null)
        val savedPassword = sharedPreferences.getString("PASSWORD", null)
        sharedPreferences.edit()
            .putBoolean("IS_LOGGED_IN", true)
            .apply()

        // Check if account exists
        if (savedEmail == null || savedPassword == null) {
            Toast.makeText(
                requireContext(),
                "No account found. Please create an account first.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Validate credentials
        if (email != savedEmail || password != savedPassword) {
            Toast.makeText(
                requireContext(),
                "Invalid Email or Password",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Login Success
        btnLogin.isEnabled = false
        btnLogin.text = "Logging in..."

        btnLogin.postDelayed({

            if (!isAdded) return@postDelayed

            btnLogin.isEnabled = true
            btnLogin.text = "Login"

            Toast.makeText(
                requireContext(),
                "Login Successful!",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(
                R.id.homeFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()
            )

        }, 1500)
    }

    /**
     * Staggered entry animations — uses requireView() since we're in a Fragment
     */
    private fun animateViews() {
        val root = requireView()
        val views = listOf(
            root.findViewById<View>(R.id.cardLogo),
            root.findViewById<View>(R.id.tvAppName),
            root.findViewById<View>(R.id.tvTagline),
            root.findViewById<View>(R.id.tvWelcome),
            root.findViewById<View>(R.id.tvSubtitle),
            root.findViewById<View>(R.id.cardEmail),
            root.findViewById<View>(R.id.cardPassword),
            root.findViewById<View>(R.id.tvForgotPassword),
            root.findViewById<View>(R.id.cardLoginBtn),
            root.findViewById<View>(R.id.llDivider),
            root.findViewById<View>(R.id.llSocial),
            root.findViewById<View>(R.id.llSignUp)
        )

        views.forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 40f
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .setStartDelay((index * 60).toLong())
                .start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // If using ViewBinding, set binding = null here to avoid memory leaks
    }
}