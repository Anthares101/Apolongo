package com.apolongo.apolongo.DB;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    class CardViewHolder extends RecyclerView.ViewHolder{
        private final TextView cardItemView;

        private CardViewHolder(View itemView){
            super(itemView);
            cardItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Card> mCards; //Cached copy of words

    CardListAdapter(Context context){mInflater = LayoutInflater.from(context);}

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.recyclerview_item, parent, false);
        return new CardViewHolder(itemView);
    }
}
