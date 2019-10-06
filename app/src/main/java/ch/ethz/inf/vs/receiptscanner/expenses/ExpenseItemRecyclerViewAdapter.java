package ch.ethz.inf.vs.receiptscanner.expenses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.ethz.inf.vs.receiptscanner.R;
import ch.ethz.inf.vs.receiptscanner.database.CommonDatabase;

public class ExpenseItemRecyclerViewAdapter extends RecyclerView.Adapter<ExpenseItemRecyclerViewAdapter.ViewHolder> {

    private List<Expense> expenses;

    public ExpenseItemRecyclerViewAdapter() {
        this.expenses = CommonDatabase.getExpenses();
    }

    public void onDatabaseUpdate(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_expenses_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.dateView.setText(expenses.get(position).date);
        holder.priceView.setText(String.format("%.2f", expenses.get(position).price));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView dateView;
        public final TextView priceView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            dateView = view.findViewById(R.id.expense_date);
            priceView = view.findViewById(R.id.expense_price);
        }
    }
}
