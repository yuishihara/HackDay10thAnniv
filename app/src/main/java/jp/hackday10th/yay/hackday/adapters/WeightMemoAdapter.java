package jp.hackday10th.yay.hackday.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jp.hackday10th.yay.hackday.R;
import jp.hackday10th.yay.hackday.models.MemoItem;

public class WeightMemoAdapter extends RecyclerView.Adapter<WeightMemoAdapter.ViewHolder> {
    private Typeface mTypeFace;
    private List<MemoItem> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View itemView) {
            this(itemView, null);

        }

        public ViewHolder(View itemView, Typeface typeface) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.weight_item);
            if (typeface != null) {
                mTextView.setTypeface(typeface);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WeightMemoAdapter(List<MemoItem> myDataset, Typeface typeface) {
        mDataset = myDataset;
        mTypeFace = typeface;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WeightMemoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, mTypeFace);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getWeight());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
