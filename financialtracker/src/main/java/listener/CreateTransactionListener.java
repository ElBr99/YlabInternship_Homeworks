package listener;

import model.Transaction;

public interface CreateTransactionListener {

    void onCreate(Transaction transaction);
}
