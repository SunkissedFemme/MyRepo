package com.example.user.instaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerviewAdapter
    extends RecyclerView.Adapter<RecyclerviewAdapter.MyHolder>{

    private  List<Followed_By> listdata;
    private  LayoutInflater mInflater;


         public RecyclerviewAdapter(Context context, List<Followed_By> listdata) {
            this.listdata = listdata;
             mInflater = LayoutInflater.from(context);
        }

        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.myview,parent,false);

            MyHolder myHolder = new MyHolder(view);
            return myHolder;
        }



    public void onBindViewHolder(MyHolder holder, int position) {
           Followed_By data = listdata.get(position);
            holder.vscore.setText(Integer.toString(data.getScore()));
            holder.vemail.setText(data.getName());
            //holder.vaddress.setText("mere");
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }


        class MyHolder extends RecyclerView.ViewHolder{
            TextView vscore, vaddress, vemail;

            public MyHolder(View itemView) {
                super(itemView);
                vscore = itemView.findViewById(R.id.vscore);
                vemail = itemView.findViewById(R.id.vemail);
             //   vaddress = (TextView) itemView.findViewById(R.id.vaddress);

            }
        }
}
