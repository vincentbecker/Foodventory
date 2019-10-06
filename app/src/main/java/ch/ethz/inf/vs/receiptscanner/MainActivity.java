package ch.ethz.inf.vs.receiptscanner;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.FirebaseApp;

import ch.ethz.inf.vs.receiptscanner.database.CommonDatabase;
import ch.ethz.inf.vs.receiptscanner.expenses.ExpensesFragment;
import ch.ethz.inf.vs.receiptscanner.inventory.InventoryDatabase;
import ch.ethz.inf.vs.receiptscanner.inventory.InventoryFragment;
import ch.ethz.inf.vs.receiptscanner.scanner.ScannerFragment;

public class MainActivity extends AppCompatActivity implements InteractionCallback {

    public static final String TAG = "MAIN_ACTIVITY";

    private final ScannerFragment scannerFragment = new ScannerFragment();
    private final InventoryFragment inventoryFragment = new InventoryFragment();
    private final ExpensesFragment expensesFragment = new ExpensesFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment activeFragment = scannerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // Initialize databases
        CommonDatabase.initalizeDatabase(getApplicationContext());
        CommonDatabase.nukeTables();

        String[] names = getResources().getStringArray(R.array.food_names);
        int[] expiry = getResources().getIntArray(R.array.food_expiry);
        String[] storage = getResources().getStringArray(R.array.food_storage);
        String[] other = getResources().getStringArray(R.array.other_items);

        FoodInformation.initialize(names, expiry, storage, other);

        fm.beginTransaction().add(R.id.main_container, expensesFragment, "3").hide(expensesFragment).commit();
        fm.beginTransaction().add(R.id.main_container, inventoryFragment, "2").hide(inventoryFragment).commit();
        fm.beginTransaction().add(R.id.main_container, scannerFragment, "1").commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_scanner:
                        Log.d(TAG, "Scanner");
                        fm.beginTransaction().hide(activeFragment).show(scannerFragment).commit();
                        activeFragment = scannerFragment;
                        return true;
                    case R.id.navigation_inventory:
                        Log.d(TAG, "Inventory");
                        fm.beginTransaction().hide(activeFragment).show(inventoryFragment).commit();
                        activeFragment = inventoryFragment;
                        return true;
                    case R.id.navigation_expenses:
                        Log.d(TAG, "Expenses");
                        fm.beginTransaction().hide(activeFragment).show(expensesFragment).commit();
                        activeFragment = expensesFragment;
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onInteraction(InteractionType type) {
        switch (type) {
            case INVENTORY_UPDATE:
                inventoryFragment.onDatabaseUpdate();
                break;
            case EXPENSE_UPDATE:
                expensesFragment.onDatabaseUpdate();
        }
    }
}
