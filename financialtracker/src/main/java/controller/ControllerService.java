package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public interface ControllerService {
    void execute(Scanner scanner);

    void execute(PrintWriter out, BufferedReader in) throws IOException;
}
