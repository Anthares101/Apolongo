package com.apolongo.apolongo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apolongo.apolongo.DB.Purchase;

import java.util.List;

public class CycleListAdapter extends RecyclerView.Adapter<CycleListAdapter.CycleViewHolder> {

    class CycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private final TextView cycleItemView;
        private ItemClickListener mItemClickListener;

        private CycleViewHolder(View itemView){
            super(itemView);
            cycleItemView = itemView.findViewById(R.id.textView);

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
    private List<Purchase> mPurchases; //Cached copy of purchases
    private ApolongoViewModel mViewModel;

    //We use the viewmodel to remove a Card Later
    CycleListAdapter(Context context, ApolongoViewModel viewModel){
        mInflater = LayoutInflater.from(context);
        mViewModel = viewModel;
    }

    @Override
    public CycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CycleViewHolder holder, int position){
        if(mPurchases != null){
            Purchase current = mPurchases.get(position);
            holder.cycleItemView.setText(current.getPurchaseName());
        } else{
            holder.cycleItemView.setText("No name");
        }

        //For every Item in the recycler list a OnClick listener is configured
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(final View view, int position, boolean isLongClick) {
                if (isLongClick) {//Long click will allow user to delete a card
                    final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Borrar compra " + mPurchases.get(position).getPurchaseName());
                    final int position_copy = position; //This variable is to evade an error
                    builder.setMessage("Eliminará definitivamente esta compra");
                    builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(view.getContext(), mPurchases.get(position_copy).getPurchaseName() + " borrada", Toast.LENGTH_LONG).show();
                            mViewModel.deletePurchase(mPurchases.get(position_copy));
                            mPurchases.remove(position_copy);
                            notifyItemRemoved(position_copy);
                            //Toast.makeText(view.getContext(), "Borrado (Es mentira)", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(view.getContext(), mPurchases.get(position).getPurchaseName() + " HOLO", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void setCards(List<Purchase> cards){
        mPurchases = cards;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mCards has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount(){
        if(mPurchases != null)
            return mPurchases.size();
        else return 0;
    }
}
