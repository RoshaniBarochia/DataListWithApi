package com.app.listdatawithapi


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.listdatawithapi.databinding.RowDataBinding


class DataListAdapter(
    private var list: ArrayList<DataList>
) : RecyclerView.Adapter<DataListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mBinding?.data=list[position]
        holder.mBinding?.position = position
//        holder.mBinding?.title?.text = list[position].title
        holder.mBinding?.album?.text = list[position].id.toString()
    }
    fun setList(listData : ArrayList<DataList>){
        list=listData
        notifyItemRangeChanged(0, list.size)
    }
    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(binding: RowDataBinding) : RecyclerView.ViewHolder(binding.root) {
        val mBinding: RowDataBinding? = DataBindingUtil.bind(itemView)

    }
}
