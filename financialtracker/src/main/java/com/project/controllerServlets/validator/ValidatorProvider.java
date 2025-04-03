package com.project.controllerServlets.validator;

import com.project.controllerServlets.*;

import java.util.HashMap;
import java.util.Map;

public class ValidatorProvider {


    private static final Map<ValidatorKey, MyValidator> validatorProcessor = new HashMap<>();

    static {

        ValidatorKey deleteUserKey = new ValidatorKey(DeleteUserServlet.class, "DELETE");
        ValidatorKey blockUserKey = new ValidatorKey(BlockUserServlet.class, "PUT");
        ValidatorKey getUserTransactionKey = new ValidatorKey(GetUserTransactionServlet.class, "GET");

        ValidatorKey changeTransInfoKey = new ValidatorKey(ChangeTransactionServlet.class, "PUT");
        ValidatorKey createTransactionKey = new ValidatorKey(CreateTransactionServlet.class, "POST");
        ValidatorKey deleteTransactionKey = new ValidatorKey(DeleteTransactionServlet.class, "DELETE");
        ValidatorKey editAccountKey = new ValidatorKey(EditAccountServlet.class, "PUT");
        ValidatorKey enterKey = new ValidatorKey(EntranceServlet.class, "POST");
        ValidatorKey getExpIncomeKey = new ValidatorKey(GetExpIncomeServlet.class, "GET");
        ValidatorKey registrationUserKey = new ValidatorKey(RegistrationServlet.class, "POST");

        EmailValidator emailValidator = new EmailValidator();
        ChangeTransactionInfoValidator changeTransactionInfoValidator = new ChangeTransactionInfoValidator();
        CreateTransactionValidator createTransactionValidator = new CreateTransactionValidator();
        IdValidator idTransactionValidator = new IdValidator();
        EditAccountValidator editAccountValidator = new EditAccountValidator();
        EntranceValidator entranceValidator = new EntranceValidator();
        GetExpIncomeValidator getExpIncomeValidator = new GetExpIncomeValidator();
        RegistrationValidator registrationValidator=new RegistrationValidator();



        validatorProcessor.put(deleteUserKey, emailValidator);
        validatorProcessor.put(blockUserKey, emailValidator);
        validatorProcessor.put(getUserTransactionKey, emailValidator );
        validatorProcessor.put(changeTransInfoKey, changeTransactionInfoValidator);
        validatorProcessor.put(createTransactionKey, createTransactionValidator);
        validatorProcessor.put(deleteTransactionKey, idTransactionValidator);
        validatorProcessor.put(editAccountKey, editAccountValidator);
        validatorProcessor.put(enterKey, entranceValidator);
        validatorProcessor.put(getExpIncomeKey, getExpIncomeValidator);
        validatorProcessor.put(registrationUserKey, registrationValidator);

    }

    public static MyValidator get(ValidatorKey validatorKey) {
        return  validatorProcessor.get(validatorKey);
    }


}
