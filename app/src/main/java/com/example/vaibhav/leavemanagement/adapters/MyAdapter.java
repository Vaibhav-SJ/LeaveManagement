package com.example.vaibhav.leavemanagement.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vaibhav.leavemanagement.EmployeeDetailsActivity;
import com.example.vaibhav.leavemanagement.R;
import com.example.vaibhav.leavemanagement.pojo.GetEmployeeDetailsModule;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private int lastPosition = -1;
    private Context context;
    private LayoutInflater inflater;
    private List<GetEmployeeDetailsModule.EmployeeDetailsBean> employeeDetailsBeanList;

    public MyAdapter(Context context, List<GetEmployeeDetailsModule.EmployeeDetailsBean> employeeDetailsBeanList)
    {
        this.context = context;
        this.employeeDetailsBeanList = employeeDetailsBeanList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.emp_details_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position)
    {
        Glide.with(context).load(employeeDetailsBeanList.get(position).getImg_url()).into(holder.image);
        holder.name.setText(employeeDetailsBeanList.get(position).getName());
        holder.phoneAns.setText(employeeDetailsBeanList.get(position).getPhone());
        holder.designationAns.setText(employeeDetailsBeanList.get(position).getDesignation());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(context, EmployeeDetailsActivity.class);
                intent.putExtra("id",employeeDetailsBeanList.get(position).getId());
                intent.putExtra("name",employeeDetailsBeanList.get(position).getName());
                intent.putExtra("phone",employeeDetailsBeanList.get(position).getPhone());
                intent.putExtra("age",employeeDetailsBeanList.get(position).getAge());
                intent.putExtra("gender",employeeDetailsBeanList.get(position).getGender());
                intent.putExtra("img_url",employeeDetailsBeanList.get(position).getImg_url());
                intent.putExtra("designation",employeeDetailsBeanList.get(position).getDesignation());
                intent.putExtra("email",employeeDetailsBeanList.get(position).getEmail());
                context.startActivity(intent);
            }
        });

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(650);
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return employeeDetailsBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.phoneAns)
        TextView phoneAns;
        @BindView(R.id.designationAns)
        TextView designationAns;
        @BindView(R.id.layout)
        RelativeLayout layout;


        ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
