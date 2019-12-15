package br.cin.ufpe.wakey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class BottomSheetFragment : Fragment() {

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onResume() {
        super.onResume()
        listView = activity!!.findViewById<ListView>(R.id.listview)

        val data = arrayOf("Home", "Work", "School")
        val adapter = RecentWakeyAdapter(activity!!.applicationContext, data)
        listView.adapter = adapter
    }

}
