package com.apolongo.apolongo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apolongo.apolongo.DB.Card;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private final TextView cardItemView;
        private ItemClickListener mItemClickListener;

        private CardViewHolder(View itemView){
            super(itemView);
            cardItemView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        //Set a listener for both Long and short click in items
        private void setItemClickListener(ItemClickListener itemClickListener){
            this.mItemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view){
            mItemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view){
            mItemClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }

    private final LayoutInflater mInflater;
    private List<Card> mCards; //Cached copy of cards
    private ApolongoViewModel mViewModel;

    //We use the viewmodel to remove a Card Later
    CardListAdapter(Context context, ApolongoViewModel viewModel){
        mInflater = LayoutInflater.from(context);
        mViewModel = viewModel;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position){
        if(mCards != null){
            Card current = mCards.get(position);
            holder.cardItemView.setText(current.getCardName());
        } else{
            holder.cardItemView.setText(R.string.no_dataYet);
        }

        //For every Item in the recycler list a OnClick listener is configured
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(final View view, int position, boolean isLongClick) {
                if (isLongClick) {//Long click will allow user to delete a card
                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Borrar tarjeta " + mCards.get(position).getCardName());
                    final int position_copy = position; //This variable is to evade an error
                    builder.setMessage("Eliminará todas las compras relacionadas");
                    builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(view.getContext(), mCards.get(position_copy).getCardName() + " borrada", Toast.LENGTH_LONG).show();
                            mViewModel.deleteCard(mCards.get(position_copy));
                            notifyItemRemoved(position_copy);
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {//Short click will take the user to the selected card purchases area
                    Intent intent = new Intent(view.getContext(), CycleListActivity.class);
                    intent.putExtra("cardName", mCards.get(position).getCardName());
                    intent.putExtra("cardCycle", mCards.get(position).getBillingCycle());
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    void setCards(List<Card> cards){
        mCards = cards;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mCards has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount(){
        if(mCards != null)
            return mCards.size();
        else return 0;
    }
}
