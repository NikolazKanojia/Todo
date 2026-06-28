package com.project.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.project.todo.databinding.FragmentAddTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        clickListeners()

        return binding.root
    }

    private fun clickListeners() {

        // Close Screen
        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }

        // Done Button
        binding.btnDone.setOnClickListener {
            saveTask()
        }

        // Save Button
        binding.btnSave.setOnClickListener {
            saveTask()
        }

        // Date Picker
        binding.dateCard.setOnClickListener {
            showDatePicker()
        }

        // Reminder Time Picker
        binding.reminderCard.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showDatePicker() {

        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->

                calendar.set(year, month, day)

                val formatter =
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

                binding.tvDate.text = formatter.format(calendar.time)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        ).show()
    }

    private fun showTimePicker() {

        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->

                val selectedTime =
                    String.format("%02d:%02d", hour, minute)

                binding.tvReminder.text = selectedTime

            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false

        ).show()
    }

    private fun saveTask() {

        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        when {

            title.isEmpty() -> {
                binding.etTitle.error = "Enter title"
                binding.etTitle.requestFocus()
            }

            description.isEmpty() -> {
                binding.etDescription.error = "Enter description"
                binding.etDescription.requestFocus()
            }

            else -> {

                Toast.makeText(
                    requireContext(),
                    "Task Saved Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // TODO Save in Room Database

                findNavController().popBackStack()
            }
        }
    }

}