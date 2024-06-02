package com.example.flats4us21.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flats4us21.data.StudentForMatcher
import com.example.flats4us21.ui.StudentForMatcherFragment

class MatchAdapter(
    fragmentActivity: FragmentActivity,
    private var students: List<StudentForMatcher>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = students.size

    override fun createFragment(position: Int): Fragment {
        val student = students[position]
        return StudentForMatcherFragment.newInstance(student)
    }

    fun updateList(newStudents: List<StudentForMatcher>) {
        students = newStudents
        notifyDataSetChanged()
    }
}
