package com.project.todo

import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.text.TextWatcher
import android.text.Editable
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.project.todo.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnTogglePassword: ImageButton
    private lateinit var btnToggleConfirmPassword: ImageButton
    private lateinit var btnSignUp: View
    private lateinit var btnBack: ImageButton
    private lateinit var tvLogin: TextView
    private lateinit var avatarContainer: FrameLayout

    // CheckBoxes for password rules
    private lateinit var checkLength: CheckBox
    private lateinit var checkNumber: CheckBox
    private lateinit var checkSpecial: CheckBox

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    lateinit var binding: FragmentSignUpBinding

    lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: TaskViewModel
    var imageUri : String=""
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

            if (uri != null) {

                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.ivAvatar)
                imageUri=uri.toString()
                binding.flAddPhoto.visibility = View.GONE

            } else {

                binding.ivAvatar.setImageResource(R.drawable.ic_person)
                binding.flAddPhoto.visibility = View.VISIBLE

            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSignUpBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(requireActivity().application)
        )[TaskViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        animateViews()
        setupListeners()
        setupPasswordValidation()
    }

    private fun initViews(view: View) {
        etFullName               = view.findViewById(R.id.etFullName)
        etEmail                  = view.findViewById(R.id.etEmail)
        etPassword               = view.findViewById(R.id.etPassword)
        etConfirmPassword        = view.findViewById(R.id.etConfirmPassword)
        btnTogglePassword        = view.findViewById(R.id.btnTogglePassword)
        btnToggleConfirmPassword = view.findViewById(R.id.btnToggleConfirmPassword)
        btnSignUp                = view.findViewById(R.id.btnSignUp)
        btnBack                  = view.findViewById(R.id.btnBack)
        tvLogin                  = view.findViewById(R.id.tvLogin)
        avatarContainer          = view.findViewById(R.id.avatarContainer)
        checkLength              = view.findViewById(R.id.checkLength)
        checkNumber              = view.findViewById(R.id.checkNumber)
        checkSpecial             = view.findViewById(R.id.checkSpecial)

        // All unchecked initially
        checkLength.isChecked = false
        checkNumber.isChecked = false
        checkSpecial.isChecked = false
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(etPassword, btnTogglePassword, isPasswordVisible)
        }

        btnToggleConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(etConfirmPassword, btnToggleConfirmPassword, isConfirmPasswordVisible)
        }

        btnSignUp.setOnClickListener {
            handleSignUp()
        }
        binding.ivAvatar.setOnClickListener {
            saveProfilePhoto()
        }
        tvLogin.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        avatarContainer.setOnClickListener {
            Toast.makeText(requireContext(), "Pick a profile photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProfilePhoto(){

        pickMedia.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
    private fun setupPasswordValidation() {
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validatePasswordRules(s?.toString() ?: "")
            }
        })
    }

    private fun validatePasswordRules(password: String) {
        checkLength.isChecked  = password.length >= 8
        checkNumber.isChecked  = password.any { it.isDigit() }
        checkSpecial.isChecked = password.any { !it.isLetterOrDigit() }
    }

    private fun togglePasswordVisibility(
        editText: EditText,
        toggleButton: ImageButton,
        isVisible: Boolean
    ) {
        editText.transformationMethod = if (isVisible) {
            toggleButton.setImageResource(R.drawable.ic_eye_off)
            HideReturnsTransformationMethod.getInstance()
        } else {
            toggleButton.setImageResource(R.drawable.ic_eye)
            PasswordTransformationMethod.getInstance()
        }
        editText.setSelection(editText.text.length)
    }

    private fun handleSignUp() {
        val fullName        = etFullName.text.toString().trim()
        val email           = etEmail.text.toString().trim()
        val password        = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()


        when {
            fullName.isEmpty() -> {
                etFullName.error = "Please enter your full name"
                etFullName.requestFocus()
            }
            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                etEmail.error = "Please enter a valid email address"
                etEmail.requestFocus()
            }
            password.length < 8 -> {
                etPassword.error = "Password must be at least 8 characters"
                etPassword.requestFocus()
            }
            !password.any { it.isDigit() } -> {
                etPassword.error = "Password must include at least one number"
                etPassword.requestFocus()
            }
            !password.any { !it.isLetterOrDigit() } -> {
                etPassword.error = "Password must include at least one special character"
                etPassword.requestFocus()
            }
            password != confirmPassword -> {
                etConfirmPassword.error = "Passwords do not match"
                etConfirmPassword.requestFocus()
            }
            else -> {

                 sharedPreferences = requireContext().getSharedPreferences(
                    "UserData",
                    android.content.Context.MODE_PRIVATE
                )

                val editor = sharedPreferences.edit()

                editor.putString("FULL_NAME", fullName)
                editor.putString("EMAIL", email)
                editor.putString("PASSWORD", password)
                editor.putBoolean("IS_LOGGED_IN", true)
                editor.putString("PROFILE_PHOTO_URI", imageUri.toString())
                editor.apply()

                Toast.makeText(
                    requireContext(),
                    "Account created successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                // Clear old tasks
                viewModel.deleteAllTasks()
                Toast.makeText(requireContext(), "Account created successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.homeFragment,
                    null,
                    androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.signUpFragment, true)
                        .build()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    /**
     * Staggered entry animations — uses requireView() since we're in a Fragment
     */
    private fun animateViews() {
        val root = requireView()
        val views = listOf(
            root.findViewById<View>(R.id.btnBack),
            root.findViewById<View>(R.id.ivAvatar),
            root.findViewById<View>(R.id.tvTitle),
            root.findViewById<View>(R.id.tvSubtitle),
            root.findViewById<View>(R.id.parentFullName),
            root.findViewById<View>(R.id.parentEmail),
            root.findViewById<View>(R.id.parentPassword),
            root.findViewById<View>(R.id.ConfirmPasswordContainer),
            root.findViewById<View>(R.id.parentcheckboxes),
            root.findViewById<View>(R.id.btnSignUp),
            root.findViewById<View>(R.id.parentAlreadyHaveAccount)
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
}