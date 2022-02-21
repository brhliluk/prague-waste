package cz.brhliluk.android.praguewaste.ui.view

import cz.brhliluk.android.praguewaste.R

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object Search : BottomNavItem("Search", R.drawable.ic_search,"search")
    object Nearby: BottomNavItem("Nearby",R.drawable.ic_nearby,"nearby")
    object Settings: BottomNavItem("Settings",R.drawable.ic_settings,"settings")
}