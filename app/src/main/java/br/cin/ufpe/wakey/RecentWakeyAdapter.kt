package br.cin.ufpe.wakey

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class RecentWakeyAdapter(private val context: Context,
                         private val dataSource: MutableList<Wakey>
) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_item_wakey, parent, false)

        val titleTextView = rowView.findViewById<TextView>(R.id.wakey_list_title)
        val addressTextView = rowView.findViewById<TextView>(R.id.wakey_list_address)

        // TODO: change that for the actual class object
        val wakey = getItem(position) as Wakey
        titleTextView.text = wakey.name
        addressTextView.text = "${wakey.radius.toInt()}m radius"

        return rowView
    }
}
