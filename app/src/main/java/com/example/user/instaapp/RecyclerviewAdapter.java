package com.example.user.instaapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class RecyclerviewAdapter
    extends RecyclerView.Adapter<RecyclerviewAdapter.MyHolder>{

    private  List<Followed_By> listdata;
    private  LayoutInflater mInflater;
    private Context context;


         public RecyclerviewAdapter(Context context, List<Followed_By> listdata) {
            this.listdata = listdata;
            this.context=context;
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
           double score = data.getScore();
           DecimalFormat df2 = new DecimalFormat("#.##");
        Drawable dw_red = context.getResources().getDrawable(R.drawable.roun_rect_red);
        Drawable dw_green = context.getResources().getDrawable(R.drawable.roun_rect_orange);
        if(score!=0){
            holder.vscore.setCompoundDrawablesWithIntrinsicBounds(dw_red, null, null, null);
        }
        else{
            holder.vscore.setCompoundDrawablesWithIntrinsicBounds(dw_green, null, null, null);
        }
            holder.vscore.setText(df2.format(score));
            holder.vemail.setText(data.getName());
            holder.vcategory.setText(data.categoryWithRisk);
      //  Picasso.with(context).load("https://www.instagram.com/p/BxiOZtSJq4b/").into(holder.vProfileImage);
            //holder.vProfileImage.
            //holder.vaddress.setText("mere");
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }


        class MyHolder extends RecyclerView.ViewHolder{
            TextView vscore, vaddress, vemail, vcategory;
            ImageView vProfileImage;

            public MyHolder(View itemView) {
                super(itemView);
                vscore = itemView.findViewById(R.id.vscore);
                vemail = itemView.findViewById(R.id.vemail);
                vcategory = itemView.findViewById(R.id.vcategory);
                vProfileImage = itemView.findViewById(R.id.vProfileImage);
             //   vaddress = (TextView) itemView.findViewById(R.id.vaddress);

            }
        }
}
