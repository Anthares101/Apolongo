package com.apolongo.apolongo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apolongo.apolongo.DB.Card;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private final TextView cardItemView;
        private ItemClickListener mItemClickListener;

        private CardViewHolder(View itemView){
            super(itemView);
            cardItemView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            //An OnClick listener is configured
            setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(final View view, int position, boolean isLongClick) {
                    if (!isLongClick) {//Short click will take the user to the selected card purchases area
                        Intent intent = new Intent(view.getContext(), CycleListActivity.class);
                        intent.putExtra("cardId", mCards.get(position).getCardId());
                        intent.putExtra("cardName", mCards.get(position).getCardName());
                        intent.putExtra("cardCycle", mCards.get(position).getBillingCycle());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }

        //Create the contextual Menu
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            MenuItem Edit = menu.add(Menu.NONE, 1, 1, R.string.action_edit);
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, R.string.action_delete);

            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        //Menu item listener
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener(){
          @Override
          public boolean onMenuItemClick(MenuItem item){
              switch (item.getItemId()){
                  case 1://Launch CardActivity
                      Intent intent = new Intent(mInflater.getContext(), CardActivity.class);

                      intent.putExtra("CardId",mCards.get(getAdapterPosition()).getCardId());
                      intent.putExtra("CardName",mCards.get(getAdapterPosition()).getCardName());
                      intent.putExtra("BillingCycle",mCards.get(getAdapterPosition()).getBillingCycle());

                      ((Activity)(mInflater.getContext())).startActivityForResult(intent, MainActivity.UPDATE_CARD_ACTIVITY_REQUEST_CODE);
                      break;
                  case 2://Delete a Card
                      final AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
                      builder.setTitle("Borrar tarjeta " + mCards.get(getAdapterPosition()).getCardName());
                      final int position_copy = getAdapterPosition(); //This variable is to evade an error
                      builder.setMessage("Eliminará todas las compras relacionadas");
                      builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              Toast.makeText(mInflater.getContext(), mCards.get(position_copy).getCardName() + " borrada", Toast.LENGTH_LONG).show();
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
                      break;
              }

              return true;
          }
        };

        //Set a listener for both Long and short click in items
        private void setItemClickListener(ItemClickListener itemClickListener){
            this.mItemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view){
            mItemClickListener.onClick(view, getAdapterPosition(), false);
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

            String content = current.getCardName() + "\nCiclo: Día " + String.valueOf(current.getBillingCycle());
            holder.cardItemView.setText(content);
        } else{
            holder.cardItemView.setText(R.string.no_dataYet);
        }
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
