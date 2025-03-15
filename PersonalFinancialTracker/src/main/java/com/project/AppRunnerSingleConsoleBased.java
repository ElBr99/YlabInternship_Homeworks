package com.project;

import com.project.utils.UI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRunnerSingleConsoleBased {

    private static final ExecutorService executor = Executors.newFixedThreadPool(1); // Пул потоков

    public static void main(String[] args) {
        System.out.println("Многопользовательское консольное приложение (имитация с многопоточностью)");
        executor.submit(UI::start);
    }
}