package munik.androidprojects.tradeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class customeAdapter(var arraylist : ArrayList<dataModel>) : RecyclerView.Adapter<customeAdapter.viewHolder>() ,Filterable {
var arrayListFilter = ArrayList<dataModel>()

    init {
        arrayListFilter = arraylist
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_holder,parent,false)
        return viewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayListFilter.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val user : dataModel = arrayListFilter[position]
        holder.textViewName?.text = user.header
    }
    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textViewName = itemView.findViewById(R.id.description)as TextView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()) {
                    arrayListFilter = arraylist
                } else{
                    val resultList =ArrayList<dataModel>()
                    for(row in arraylist) {
                        if(row.header.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))){
                            resultList.add(row)
                        }
                    }
                    arrayListFilter = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = arrayListFilter
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayListFilter = results?.values as ArrayList<dataModel>
                notifyDataSetChanged()
            }

        }
    }
}