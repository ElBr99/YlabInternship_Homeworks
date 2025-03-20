package com.project.controller;

import com.project.dtos.UIAction;
import com.project.listener.CreateTransactionListener;
import com.project.listener.DeleteTransactionListener;
import com.project.listener.GoalProgressListener;
import com.project.listener.NotificationTransactionListener;
import com.project.repository.*;
import com.project.service.*;
import com.project.service.impl.*;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@UtilityClass
public class ControllerProvider {

    public static final Map<UIAction, ControllerService> controllerServiceMap = new HashMap<>();


    static {
        UserRepository userRepository = new UserRepositoryImpl();
        UserService userService = new UserServiceImpl(userRepository);
        LoginService loginService = new LoginServiceImpl(userService);
        GoalRepository goalRepository = new GoalRepositoryImpl();
        BudgetRepository budgetRepository = new BudgetRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepository();
        FinancialService financialService = new FinancialServiceImpl(transactionRepository);
        GoalService goalService = new GoalServiceImpl(goalRepository);
        NotificationService notificationService = new EmailNotificationService();
        BudgetService budgetService = new BudgetServiceImpl(budgetRepository);
        List<CreateTransactionListener> createTransactionListenerList = new ArrayList<>();
        List<DeleteTransactionListener> deleteTransactionListenerList = new ArrayList<>();
        createTransactionListenerList.add(new GoalProgressListener(goalService));
        createTransactionListenerList.add(new NotificationTransactionListener(notificationService, budgetService, financialService));
        deleteTransactionListenerList.add(new GoalProgressListener(goalService));
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository, createTransactionListenerList, deleteTransactionListenerList);

        RegistrationController registrationController = new RegistrationController(userService);
        EntranceController entranceController = new EntranceController(loginService);
        CreateTransactionController createTransactionController = new CreateTransactionController(transactionService);
        ChangeTransactionController changeTransactionController = new ChangeTransactionController(transactionService);
        DeleteTransactionController deleteTransactionController = new DeleteTransactionController(transactionService);
        ViewTransactionController viewTransactionController = new ViewTransactionController(transactionService);
        ViewExpendituresController viewExpendituresController = new ViewExpendituresController(financialService);
        GetExpIncomeController getExpIncomeController = new GetExpIncomeController(financialService);
        GetFinancialReportController getFinancialReportController = new GetFinancialReportController(financialService);
        GetUserTransactionController getUserTransactionController = new GetUserTransactionController(transactionService);
        BlockUserController blockUserController = new BlockUserController(userService);
        DeleteAccountController deleteAccountController = new DeleteAccountController(userService);
        DeleteUserController deleteUserController = new DeleteUserController(userService);
        EditAccountController editAccountController = new EditAccountController(userService);
        ViewCurrentFinStatementController viewCurrentFinStatementController = new ViewCurrentFinStatementController(financialService);
        FilterTransactionsController filterTransactionsController = new FilterTransactionsController(transactionService);


        controllerServiceMap.put(UIAction.action1, registrationController);
        controllerServiceMap.put(UIAction.action2, entranceController);
        controllerServiceMap.put(UIAction.action3, getUserTransactionController);
        controllerServiceMap.put(UIAction.action4, blockUserController);
        controllerServiceMap.put(UIAction.action5, deleteUserController);
        controllerServiceMap.put(UIAction.action6, editAccountController);
        controllerServiceMap.put(UIAction.action7, deleteAccountController);
        controllerServiceMap.put(UIAction.action8, createTransactionController);
        controllerServiceMap.put(UIAction.action9, changeTransactionController);
        controllerServiceMap.put(UIAction.action10, deleteTransactionController);
        controllerServiceMap.put(UIAction.action11, viewTransactionController);
        controllerServiceMap.put(UIAction.action12, viewCurrentFinStatementController);
        controllerServiceMap.put(UIAction.action13, getExpIncomeController);
        controllerServiceMap.put(UIAction.action14, viewExpendituresController);
        controllerServiceMap.put(UIAction.action15, getFinancialReportController);
        controllerServiceMap.put(UIAction.action16, filterTransactionsController);

    }
}
