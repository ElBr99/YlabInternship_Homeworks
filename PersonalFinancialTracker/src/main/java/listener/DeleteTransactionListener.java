package listener;

import model.Transaction;

public interface DeleteTransactionListener {

    void onDelete(Transaction transaction);
}
