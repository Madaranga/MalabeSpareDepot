package com.example.malabesparedepot.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    private static final String LOG_FILE = "audit_log.txt";

    public static void logAction(String action, String partCode, int quantity) {

        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.printf("[%s] ACTION: %s | PART: %s | QTY: %d%n", timestamp, action, partCode, quantity);

        } catch (IOException e) {
            System.err.println("Failed to write to audit log file: "+ e.getMessage());
        }


    }
}
