package com.example.dangdangee.board



import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dangdangee.R
import com.example.dangdangee.Utils.FBRef
import com.example.dangdangee.databinding.RvItemListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class CustomAdapter(val item : ArrayList<BoardModel>) : RecyclerView.Adapter<CustomAdapter.Viewholder>() {
    private val boardKeyList = arrayListOf<String>()
    private val boardDataList = arrayListOf<BoardModel>()
    private val TAG = HomeFragment::class.java.simpleName


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_list,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: CustomAdapter.Viewholder, position: Int) {
        getData()

        holder.title.text=item.get(position).title
        holder.writer.text=item.get(position).uid
        val ctext = holder.itemView.context

        holder.itemView.setOnClickListener{
            onClick(ctext,position)
        }
    }



    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.rv_title)
        val writer = itemView.findViewById<TextView>(R.id.rv_writer)
        val photos = itemView.findViewById<ImageView>(R.id.rv_dogProfile)
    }


    fun onClick(context: Context, position: Int) {
        val intent = Intent(context,PostActivity::class.java)
        intent.putExtra("key",boardKeyList[position])
        context.startActivity(intent)
    }


    fun getData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardKeyList.reverse()
                boardDataList.reverse()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }



}
