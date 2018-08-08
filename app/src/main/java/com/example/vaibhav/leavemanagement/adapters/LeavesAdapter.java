package com.example.vaibhav.leavemanagement.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.example.vaibhav.leavemanagement.R;
import com.example.vaibhav.leavemanagement.SanctionedLeavesActivity;
import com.example.vaibhav.leavemanagement.pojo.LeaveDetailsModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeavesAdapter  extends RecyclerView.Adapter<LeavesAdapter.ViewHolder>
{
    List<LeaveDetailsModel.LeaveDetailsBean> leaveDetails;
    private int lastPosition = -1;
    private Context context;
    private LayoutInflater inflater;

    public LeavesAdapter(Context context, List<LeaveDetailsModel.LeaveDetailsBean> leaveDetails)
    {
        this.context = context;
        this.leaveDetails = leaveDetails;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.leave_details_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.dateAns.setText(leaveDetails.get(position).getLeaveSanctionedOn());
        holder.daysAns.setText(leaveDetails.get(position).getDays());
        holder.remarkAns.setText(leaveDetails.get(position).getRemarks());
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
        return leaveDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.dateAns)
        TextView dateAns;
        @BindView(R.id.daysAns)
        TextView daysAns;
        @BindView(R.id.remarkAns)
        TextView remarkAns;

         ViewHolder(View itemView)
         {
             super(itemView);
             ButterKnife.bind(this,itemView);
         }
    }
}
