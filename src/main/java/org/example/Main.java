package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        AuditManager auditManager = new AuditManager(2, Path.of("src","main","resources").toString());
        auditManager.addRecord("user1", LocalDateTime.now());
        auditManager.addRecord("user2", LocalDateTime.now());
        auditManager.addRecord("user3", LocalDateTime.now());

    }
}