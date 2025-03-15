package com.project;

import com.project.dtos.UIAction;
import com.project.utils.SecurityContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.project.controller.ControllerProvider.controllerServiceMap;

public class AppRunnerSocketBased {
    public static final int USER_COUNT = 10;
    private static final int PORT = 12345;
    private static final ExecutorService executor = Executors.newFixedThreadPool(USER_COUNT);

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новый клиент подключился: " + clientSocket.getInetAddress().getHostAddress());
                executor.submit(() -> handleClient(clientSocket));
            }
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            while (true) {
                if (SecurityContext.isEmptyContext()) {
                    out.println("1. Зарегистрироваться");
                    out.println("2. Войти");
                    out.println("Введите действие (1 или 2):");
                } else if (SecurityContext.getCurrentUserEmail().equals("admin@mail.ru")) {
                    out.println("3. Просмотреть транзакции пользователя");
                    out.println("4. Заблокировать пользователя");
                    out.println("5. Удалить пользователя");
                    out.println("Введите действие (3, 4 или 5):");
                } else {
                    out.println("6. Редактировать профиль");
                    out.println("7. Удалить аккаунт");
                    out.println("8. Создать транзакцию");
                    out.println("9. Редактировать транзакцию");
                    out.println("10. Удалить транзакцию");
                    out.println("11. Посмотреть все транзакции");
                    out.println("12. Посмотреть текущий баланс");
                    out.println("13. Получить доход и расход за период");
                    out.println("14. Посмотреть расходы по видам");
                    out.println("15. Сформировать отчет о фин состоянии");
                    out.println("16. Отфильтровать транзакции");
                    out.println("Введите действие (6-16):");
                }

                String inputLine = in.readLine();
                if (inputLine == null) {
                    break; // Клиент закрыл соединение
                }

                int actionValue = Integer.parseInt(inputLine);
                UIAction action = UIAction.findByValue(actionValue);
                controllerServiceMap.get(action).execute(out, in);
            }
        } catch (IOException e) {
            System.err.println("Ошибка обработки клиента: " + e.getMessage());
        }
    }
}
