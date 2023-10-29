package org.example;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class AuditManager {

    private static final String DEFAULT_FILE_NAME = "audit_0.csv";

    private final int maxLinesPerFile;
    private final String directoryPath;

    public AuditManager(int maxLinesPerFile, String directoryPath) {
        this.maxLinesPerFile = maxLinesPerFile;
        this.directoryPath = directoryPath;
    }

    public void addRecord(String userName, LocalDateTime eventTime) throws IOException {
        List<Path> filePaths;
        try (Stream<Path> sorted = Files.list(Path.of(directoryPath)).sorted()){
            filePaths = sorted.toList();
        }

        String newRecord = userName + ";" + eventTime + "\n";

        if (filePaths.isEmpty()) {
            Path newFilePath = Path.of(directoryPath, DEFAULT_FILE_NAME);
            Files.write(newFilePath, newRecord.getBytes());
            return;
        }

        int lastFilePathIndex = filePaths.size() - 1;
        Path lastFilePath = filePaths.get(lastFilePathIndex);
        List<String> lines = Files.readAllLines(lastFilePath);

        if (lines.size() < maxLinesPerFile) {
            Files.write(lastFilePath, newRecord.getBytes(), StandardOpenOption.APPEND);
        } else {
            int newFileIndex = lastFilePathIndex + 1;
            String newFileName = "audit_" + newFileIndex + ".csv";
            Path newFilePath = Path.of(directoryPath, newFileName);
            Files.write(newFilePath, newRecord.getBytes());

        }
    }
}
