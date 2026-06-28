package com.project.todo

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.todo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

private lateinit var binding: FragmentHomeBinding
    private lateinit var tvGreeting: TextView
    private lateinit var tvUserName: TextView
    private lateinit var ivNotification: ImageView
    private lateinit var etSearch: EditText
    private lateinit var tvProgressCount: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgressPercent: TextView
    private lateinit var tvTasksHeader: TextView
    private lateinit var tvEdit: TextView
    private lateinit var rvTasks: RecyclerView
    private lateinit var fabAdd: CardView

    private lateinit var sharedPreferences: SharedPreferences
    private val taskList = mutableListOf(
        TaskItem(1, "Buy Groceries",      "Milk, Bread, Eggs",  isCompleted = false, isStarred = false),
        TaskItem(2, "Complete UI Design", "TaskFlow App",       isCompleted = false, isStarred = false),
        TaskItem(3, "Gym Workout",        "Chest & Triceps",    isCompleted = true,  isStarred = true),
        TaskItem(4, "Read Book",          "Atomic Habits",      isCompleted = false, isStarred = false),
        TaskItem(5, "Read ",          "",      isCompleted = false, isStarred = true),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(
            "UserData",
            android.content.Context.MODE_PRIVATE
        )
        initViews(view)
        setupGreeting()
        setupRecyclerView()
        setupClickListeners()
        animateViews()
    }

    private fun initViews(view: View) {
        tvGreeting       = view.findViewById(R.id.tvGreeting)
        tvUserName       = view.findViewById(R.id.tvUserName)
        ivNotification   = view.findViewById(R.id.ivNotification)
        etSearch         = view.findViewById(R.id.etSearch)
        tvProgressCount  = view.findViewById(R.id.tvProgressCount)
        progressBar      = view.findViewById(R.id.progressBar)
        tvProgressPercent= view.findViewById(R.id.tvProgressPercent)
        tvTasksHeader    = view.findViewById(R.id.tvTasksHeader)
        tvEdit           = view.findViewById(R.id.tvEdit)
        rvTasks          = view.findViewById(R.id.rvTasks)
        fabAdd           = view.findViewById(R.id.fabAdd)
    }

    private fun setupGreeting() {
        val calendar = java.util.Calendar.getInstance()
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)

        val greeting = when {
            hour in 5..11 -> "Good Morning"
            hour in 12..16 -> "Good Afternoon"
            hour in 17..20 -> "Good Evening"
            else -> "Good Night"
        }

        tvGreeting.text = greeting
        val userName = sharedPreferences.getString("FULL_NAME", "User")

        val formattedName = userName?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase()
            else it.toString()
        }
        tvUserName.text = formattedName+"\uD83D\uDC4B"
        // "6/10" with "Completed" smaller and grey
        updateProgressText()
    }


    private fun updateProgressText() {
        val completed = taskList.count { it.isCompleted }
        val total     = taskList.size
        val percent   = if (total > 0) (completed * 100) / total else 0

        // Spannable: "6/10 " bold purple + "Completed" smaller grey
        val countStr = "$completed/$total "
        val fullStr  = "${countStr}Completed"
        val spannable = SpannableString(fullStr)
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#1a1a2e")),
            0, countStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            RelativeSizeSpan(0.65f),
            countStr.length, fullStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#999aaa")),
            countStr.length, fullStr.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvProgressCount.text = spannable

        progressBar.max      = 100
        progressBar.progress = percent
        tvProgressPercent.text = "$percent%"
    }


    private fun setupRecyclerView() {
        rvTasks.setHasFixedSize(true)
        rvTasks.layoutManager = LinearLayoutManager(requireContext())

        rvTasks.adapter = TaskAdapter(
            taskList,

            onCheckToggle = {
                updateProgressText()
            },

            onStarToggle = {
                updateProgressText()
            }
        )
        Log.d("TASK_SIZE", taskList.size.toString())
    }

    private fun setupClickListeners() {
        ivNotification.setOnClickListener {
            Toast.makeText(requireContext(), "Notifications", Toast.LENGTH_SHORT).show()
        }
        tvEdit.setOnClickListener {
            Toast.makeText(requireContext(), "Edit Tasks", Toast.LENGTH_SHORT).show()
        }
        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTaskFragment)
            Toast.makeText(requireContext(), "Add new task", Toast.LENGTH_SHORT).show()

        }
    }

    private fun animateViews() {
        val root = requireView()
        val views = listOf(
            root.findViewById<View>(R.id.llHeader),
            root.findViewById<View>(R.id.cardSearch),
            root.findViewById<View>(R.id.cardProgress),
            root.findViewById<View>(R.id.llTasksHeader),
            root.findViewById<View>(R.id.rvTasks),
            root.findViewById<View>(R.id.fabAdd)
        )
        views.forEachIndexed { i, v ->
            v.alpha = 0f
            v.translationY = 30f
            v.animate().alpha(1f).translationY(0f)
                .setDuration(380).setStartDelay((i * 70).toLong()).start()
        }
    }


}