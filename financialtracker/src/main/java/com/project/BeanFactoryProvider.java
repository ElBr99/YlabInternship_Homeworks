package com.project;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class BeanFactoryProvider {

    public static final Map<Class<?>, Object> beanFactoryProvider = new HashMap<>();


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
        ObjectMapper objectMapper = new ObjectMapper();

        beanFactoryProvider.put(UserService.class, userService);
        beanFactoryProvider.put(LoginService.class, loginService);
        beanFactoryProvider.put(TransactionService.class, transactionService);
        beanFactoryProvider.put(ObjectMapper.class, objectMapper);
        beanFactoryProvider.put(FinancialService.class, financialService);

    }

    public static <T> T get(Class<T> clazz) {
        return (T) beanFactoryProvider.get(clazz);
    }
}
