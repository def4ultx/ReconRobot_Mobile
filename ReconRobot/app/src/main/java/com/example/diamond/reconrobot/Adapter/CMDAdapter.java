package com.example.diamond.reconrobot.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.diamond.reconrobot.Model.CommandModel;
import com.example.diamond.reconrobot.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diamond on 14/3/2018 AD.
 */

public class CMDAdapter extends RecyclerView.Adapter<CMDAdapter.items> {

    public Context context;
    public List<CommandModel> modelList;

    public CMDAdapter(Context context) {
        this.context    = context;
        modelList       = new ArrayList<>();
        modelList.clear();
    }

    @Override
    public items onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new items(v);
    }

    @Override
    public void onBindViewHolder(CMDAdapter.items holder, int position) {
        if(modelList.get(position).getCommand().equalsIgnoreCase("f"))
        {
            holder.textViewCommand.setText("Forward");
            holder.textViewDistance.setText(modelList.get(position).getDistance()+"");

        }
        else if(modelList.get(position).getCommand().equalsIgnoreCase("b"))
        {
            holder.textViewCommand.setText("Backward");
            holder.textViewDistance.setText(modelList.get(position).getDistance()+"");

        }
        else if(modelList.get(position).getCommand().equalsIgnoreCase("l"))
        {
            holder.textViewCommand.setText("Turn Left");
            holder.textViewDistance.setText("0");

        }
        else if(modelList.get(position).getCommand().equalsIgnoreCase("r"))
        {
            holder.textViewCommand.setText("Turn Right");
            holder.textViewDistance.setText("0");

        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class items extends RecyclerView.ViewHolder{

        TextView textViewCommand;
        TextView textViewDistance;

        public items(View itemView){
            super(itemView);
            textViewCommand     = (TextView)itemView.findViewById(R.id.textViewCMD);
            textViewDistance    = (TextView)itemView.findViewById(R.id.textViewDistance);
        }
    }

    public void setModel(List<CommandModel> model)
    {
        modelList.clear();
        modelList.addAll(model);
        notifyDataSetChanged();
    }
}
