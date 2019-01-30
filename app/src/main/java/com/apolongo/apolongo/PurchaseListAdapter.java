package com.apolongo.apolongo;

import android.app.Activity;
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

import com.apolongo.apolongo.DB.Purchase;

import java.io.PrintStream;
import java.util.List;

public class PurchaseListAdapter extends RecyclerView.Adapter<PurchaseListAdapter.PurchaseViewHolder> {

    class PurchaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private final TextView mPurchaseItemView;
        private ItemClickListener mItemClickListener;

        private PurchaseViewHolder(View itemView){
            super(itemView);
            mPurchaseItemView = itemView.findViewById(R.id.textView);

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
    private final Cycle mCycle;
    private List<Purchase> mPurchases; //Cached copy of purchases
    private ApolongoViewModel mViewModel;

    //We use the viewmodel to remove a Card Later
    PurchaseListAdapter(Context context, ApolongoViewModel viewModel, Cycle cycle){
        mInflater = LayoutInflater.from(context);
        mViewModel = viewModel;
        mCycle = cycle;
    }

    @Override
    public PurchaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PurchaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PurchaseViewHolder holder, int position){
        if(mPurchases != null){
            Purchase current = mPurchases.get(position);
            String content = current.getPurchaseName() + "\nPrecio: " + current.getPurchasePrice() + "€";
            holder.mPurchaseItemView.setText(content);
        } else{
            holder.mPurchaseItemView.setText("No name");
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
                } else {//Short click will take the user to the selected purchase info
                    Intent intent = new Intent(view.getContext(), PurchaseActivity.class);
                    intent.putExtra("PurchaseId", mPurchases.get(position).getPurchaseId());
                    intent.putExtra("PurchaseName",mPurchases.get(position).getPurchaseName());
                    intent.putExtra("PurchasePrice", mPurchases.get(position).getPurchasePrice());
                    intent.putExtra("PurchaseDate", mPurchases.get(position).getPurchaseDate());
                    intent.putExtra("PurchaseDesc", mPurchases.get(position).getPurchaseSDescp());
                    intent.putExtra("PurchaseCardName",mPurchases.get(position).getPurchaseCardName());

                    //Needed for the back button action
                    intent.putExtra("StartDate", mCycle.getStart());
                    intent.putExtra("FinishDate", mCycle.getFinish());
                    ((Activity)(view.getContext())).startActivityForResult(intent, PurchaseListActivity.UPDATE_PURCHASE_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }

    void setPurchases(List<Purchase> cards){
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
