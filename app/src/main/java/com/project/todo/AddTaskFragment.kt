package com.project.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.todo.databinding.FragmentAddTaskBinding


class AddTaskFragment : Fragment() {
    lateinit var binding: FragmentAddTaskBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddTaskBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

}