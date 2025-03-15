package com.project.utils;

import com.project.controller.ControllerProvider;
import com.project.controller.ControllerService;
import com.project.dtos.UIAction;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Scanner;

@UtilityClass
public class UI {

    public static void start() {
        var controllerMap = ControllerProvider.controllerServiceMap;

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if (SecurityContext.isEmptyContext()) {
                    System.out.println("1. Зарегистрироваться");
                    System.out.println("2. Войти");

                    chooseAction(scanner, controllerMap);
                } else if (SecurityContext.getCurrentUserEmail().equals("admin@mail.ru")) {
                    System.out.println("3. Просмотреть транзакции пользователя");
                    System.out.println("4. Заблокировать пользователя");
                    System.out.println("5. Удалить пользователя");
                    chooseAction(scanner, controllerMap);

                } else {
                    System.out.println("6. Редактировать профиль");
                    System.out.println("7. Удалить аккаунт");
                    System.out.println("8. Создать транзакцию");
                    System.out.println("9. Редактировать транзакцию");
                    System.out.println("10. Удалить транзакцию");
                    System.out.println("11. Посмотреть все транзакции");
                    System.out.println("12. Посмотреть текущий баланс");
                    System.out.println("13. Получить доход и расход за период");
                    System.out.println("14. Посмотреть расходы по видам");
                    System.out.println("15. Сформировать отчет о фин состоянии");
                    System.out.println("16. Отфильтровать транзакции");
                    chooseAction(scanner, controllerMap);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void chooseAction(Scanner scanner, Map<UIAction, ControllerService> controllerMap) {
        String lines = scanner.nextLine();
        String[] lineAr = lines.split("\\.");
        String str = lineAr[0];
        int val = Integer.parseInt(str);
        UIAction enumVal = UIAction.findByValue(val);
        controllerMap.get(enumVal).execute(scanner);
    }
}
