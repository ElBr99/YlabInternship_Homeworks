package com.project.listener;

import com.project.model.Transaction;

public interface DeleteTransactionListener {

    void onDelete(Transaction transaction);
}
