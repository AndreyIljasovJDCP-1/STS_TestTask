package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@DisplayName("Тест класса AuditManager")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuditManagerTest {
    private static final String DEFAULT_FILE_NAME = "audit_0.csv";
    private static final Path DIRECTORY_PATH = Path.of("src", "main", "resources");
    private static final int MAX_LINES = 3;
    private AuditManager auditManager;
    private static final Logger logger = LoggerFactory.getLogger(AuditManagerTest.class);

    @BeforeEach
    void SetUp() {
        auditManager = new AuditManager(MAX_LINES, DIRECTORY_PATH.toString());
    }

    @Order(1)
    @DisplayName("Тест: проверка количества файлов")
    @ParameterizedTest(name = "Records add: {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 11})
    void check(int records) throws IOException {
        for (int i = 0; i < records; i++) {
            auditManager.addRecord("user" + (i + 1), LocalDateTime.now());
        }
        List<Path> filePaths;
        try (Stream<Path> sorted = Files.list(DIRECTORY_PATH).sorted()) {
            filePaths = sorted.toList();
        }
        int expectedFiles = records / MAX_LINES + (records % MAX_LINES == 0 ? 0 : 1);
        logger.info("Records: {} Count files: {}", records, expectedFiles);
        Assertions.assertEquals(expectedFiles, filePaths.size());
    }

    @Order(2)
    @DisplayName("Тест: проверка количества записей в файле.")
    @ParameterizedTest(name = "Records add: {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 11})
    void checkMaxLinesPerFile(int records) throws IOException {
        for (int i = 0; i < records; i++) {
            auditManager.addRecord("user" + (i + 1), LocalDateTime.now());
        }
        List<Path> filePaths;
        try (Stream<Path> sorted = Files.list(DIRECTORY_PATH).sorted()) {
            filePaths = sorted.toList();
        }
        int expectedLines;
        for (int i = 0; i < filePaths.size(); i++) {
            Path path = filePaths.get(i);
            var listRecords = Files.readAllLines(path);
            expectedLines = MAX_LINES;
            if (i == filePaths.size() - 1) {
                expectedLines = records % MAX_LINES == 0
                        ? MAX_LINES
                        : records % MAX_LINES;
            }
            logger.info("FileName: {}  ", path.getFileName());
            logger.info("File content: {}  ", listRecords);
            logger.info("File size expected: {} File size actual: {}  ", expectedLines, listRecords.size());
            Assertions.assertEquals(expectedLines, listRecords.size());
        }
    }

    @Order(3)
    @DisplayName("Тест: проверка имени файла")
    @ParameterizedTest(name = "Records add: {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 11})
    void checkFileName(int records) throws IOException {
        for (int i = 0; i < records; i++) {
            auditManager.addRecord("user" + (i + 1), LocalDateTime.now());
        }
        List<Path> filePaths;
        try (Stream<Path> sorted = Files.list(DIRECTORY_PATH).sorted()) {
            filePaths = sorted.toList();
        }
        for (int i = 0; i < filePaths.size(); i++) {
            Path path = filePaths.get(i);
            String expectedFileName = DEFAULT_FILE_NAME.replaceFirst("0", i + "");
            String actualFileName = path.getFileName().toString();
            logger.info("ExpectedFileName: {} ActualFileName: {} ", expectedFileName, actualFileName);
            Assertions.assertEquals(expectedFileName, actualFileName);
        }
    }

    @Order(4)
    @DisplayName("Тест: проверка записей в файле.")
    @ParameterizedTest(name = "Records add: {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 11})
    void checkContentFile(int records) throws IOException {
        for (int i = 0; i < records; i++) {
            auditManager.addRecord("user" + (i + 1), LocalDateTime.now());
        }
        List<Path> filePaths;
        try (Stream<Path> sorted = Files.list(DIRECTORY_PATH).sorted()) {
            filePaths = sorted.toList();
        }
        int count = 1;
        for (Path filePath : filePaths) {
            logger.info("FileName: {}", filePath.getFileName());
            var list1 = Files.readAllLines(filePath);
            for (String record : list1) {
                String expected = "user" + count++;
                logger.info("Line: {} Expected startsWith: {}", record, expected);
                Assertions.assertTrue(record.startsWith(expected));
            }
        }
    }

    @AfterEach
    void TearDown() throws IOException {
        List<Path> filePaths;
        try (Stream<Path> sorted = Files.list(DIRECTORY_PATH).sorted()) {
            filePaths = sorted.toList();
        }
        for (Path path : filePaths) {
            Files.deleteIfExists(path);
        }
        auditManager = null;
    }

}