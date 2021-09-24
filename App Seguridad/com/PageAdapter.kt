package com.rodrigo.mezclado

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(fm: MainActivity, supportFragmentManager: FragmentManager) : FragmentStateAdapter(fm) {

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {return Fragment1()}
            1 -> {return Fragment2()}
            2 -> {return Fragment3()}
            3 -> {return Fragment4()}
            4 -> {return Fragment5()}
            else -> return Fragment1()
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}