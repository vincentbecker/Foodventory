package ch.ethz.inf.vs.receiptscanner;

public interface InteractionCallback {

    enum InteractionType{INVENTORY_UPDATE, EXPENSE_UPDATE}

    void onInteraction(InteractionType type);
}
