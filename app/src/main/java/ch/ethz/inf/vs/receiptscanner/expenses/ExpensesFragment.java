package ch.ethz.inf.vs.receiptscanner.expenses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import ch.ethz.inf.vs.receiptscanner.DatabaseListener;
import ch.ethz.inf.vs.receiptscanner.R;
import ch.ethz.inf.vs.receiptscanner.database.CommonDatabase;

public class ExpensesFragment extends Fragment implements DatabaseListener {

    private static final String TAG = "EXPENSES_FRAGMENT";

    private ExpenseItemRecyclerViewAdapter adapter;
    private TextView totalSumTextView;

    public ExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.expensesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new ExpenseItemRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        totalSumTextView = view.findViewById(R.id.totalSumTextView);

        return view;
    }

    public void onDatabaseUpdate() {
        List<Expense> expenses = CommonDatabase.getExpenses();
        adapter.onDatabaseUpdate(expenses);
        // Set the total sum
        double sum = 0;
        for (Expense e : expenses) {
            sum += e.price;
        }

        totalSumTextView.setText(String.format("%.2f", sum));
    }
}
