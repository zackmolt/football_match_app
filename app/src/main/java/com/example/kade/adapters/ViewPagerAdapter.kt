package com.example.kade.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.kade.activities.MainActivity
import com.example.kade.fragments.NextMatchFragment
import com.example.kade.fragments.PrevMatchFragment

class ViewPagerAdapter(fm: FragmentManager?, val mainActivity: MainActivity) :
    FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if (position == 0)
            return NextMatchFragment(mainActivity)
        else
            return PrevMatchFragment(mainActivity)

        return NextMatchFragment(mainActivity)
    }

    override fun getCount(): Int {
        return 2
    }

}