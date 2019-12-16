package br.cin.ufpe.wakey

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class BottomSheetFragment : Fragment() {

    private lateinit var listView: ListView
    val wakeyList = arrayOf("Home", "Work", "School")

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

        val adapter = RecentWakeyAdapter(activity!!, wakeyList)
        listView.adapter = adapter

        // Config create btn listener
        create_btn.setOnClickListener {
            Log.v("BottomSheetFragment", "Clicou no botão!!")
            openDetailActivity()
        }

        // Config wakeys list onClick method
        listview.setOnItemClickListener { _, _, position, _ ->
            val wakey = wakeyList[position]
            openDetailActivity(wakey)
        }
    }

    fun openDetailActivity(wakey: String? = null) {
        val detailIntent = WakeyDetailActivity.newIntent(activity!!, wakey)
        startActivity(detailIntent)
    }

}
