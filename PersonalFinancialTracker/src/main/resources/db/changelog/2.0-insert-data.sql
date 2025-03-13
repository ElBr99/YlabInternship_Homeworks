--liquibase formatted sql


----changeset elbr:0

  INSERT INTO entities.users (name, email, password) values
   ('Admin', 'admin@mail.ru', 'admin'),
   ('Ivan', 'ivan@mail.ru', 'ivan'),
   ('Sveta', 'sveta@mail.ru', 'sveta');


----changeset elbr:1

    INSERT INTO entities.transactions (user_email,transaction_type,amount, description,category) values
    ('ivan@mail.ru', 'INCOME', 10000, 'возврат долга', 'прочие доходы'),
    ('ivan@mail.ru', 'EXPENDITURE', 2000, 'поход в кино', 'развлечения'),
    ('sveta@mail.ru', 'INCOME', 3000, 'подарок на др от подруги', 'подарки'),
    ('sveta@mail.ru', 'INCOME', 4000, 'кэшбэк по карте', 'кэшбэк');


----changeset elbr:2

  INSERT INTO entities.goals (user_email, target_amount, current_amount) values
   ('ivan@mail.ru', 100000, 40000),
   ('sveta@mail.ru', 500000, 250000);
  );