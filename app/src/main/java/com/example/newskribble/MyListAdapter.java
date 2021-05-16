package com.example.newskribble;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private ArrayList<MyListData> list;
    private Context context;
    private ArrayList<MyListData> itemsCopy;

    public MyListAdapter(ArrayList<MyListData> list, Context context, ArrayList<MyListData> itemsCopy) {
        this.list = list;
        this.context = context;
        this.itemsCopy = itemsCopy;
    }

    @NonNull
    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, int position) {
        MyListData data = list.get(position);
        holder.title.setText(data.getTitle());

        holder.mCardView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Note.class);
                intent.putExtra("id", position);
                intent.putExtra("idThis", 1);
                Log.d("TAG", "Position: " + position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        public View mCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.user);

            mCardView = (CardView) itemView.findViewById(R.id.thing);
        }
    }

    public Context getContext(){ return context; }

    public void filter(String text) {
        for(MyListData item: itemsCopy){
            Log.d("TAG", "items are " + item.getTitle());
        }
        Log.d("TAG", "items size " + itemsCopy.size());
        list.clear();
        if(text.isEmpty()){
            list.addAll(itemsCopy);
            Log.d("TAG", "added all");
        } else{
            Log.d("TAG", "went to else");
            text = text.toLowerCase();
            Log.d("TAG", "text is " + text);
            for(MyListData item: itemsCopy){
                Log.d("TAG", "went to " + item);
                if(item.getTitle().toLowerCase().contains(text)){
                    list.add(item);
                    Log.d("TAG", "added " + item);
                }
            }
        }
        Log.d("TAG", "done");
        notifyDataSetChanged();
    }
}