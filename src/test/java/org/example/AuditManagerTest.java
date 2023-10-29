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
    private static final Logger logger = LoggerFactory.getLogger("TEST");
    private AuditManager auditManager;
    private static int MAX_LINES;

    @BeforeEach
    void SetUp() {
        MAX_LINES = 0;
    }

    @Order(1)
    @DisplayName("Тест: проверка количества файлов.")
    @RepeatedTest(value = 5, name = "Test {currentRepetition}. MaxLinesPerFile: {currentRepetition}. Records add: 5")
    void checkCountFilesMaxLinesChanges(RepetitionInfo repetitionInfo) throws IOException {
        MAX_LINES = repetitionInfo.getCurrentRepetition();
        auditManager = new AuditManager(MAX_LINES, DIRECTORY_PATH.toString());
        int records = 5;
        for (int i = 0; i < records; i++) {
            auditManager.addRecord("user" + (i + 1), LocalDateTime.now());
        }
        List<Path> filePaths;
        try (Stream<Path> sorted = Files.list(DIRECTORY_PATH).sorted()) {
            filePaths = sorted.toList();
        }
        int expectedFiles = records / MAX_LINES + (records % MAX_LINES == 0 ? 0 : 1);
        logger.info("Records: {} MAX_LINES: {} Files: {}",
                records, MAX_LINES, expectedFiles);
        Assertions.assertEquals(expectedFiles, filePaths.size());
    }

    @Order(2)
    @DisplayName("Тест: проверка количества файлов.")
    @ParameterizedTest(name = "Test {index}. MaxLinesPerFile: 3. Records add: {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void checkCountFilesCountRecordsChanges(int records) throws IOException {
        MAX_LINES = 3;
        auditManager = new AuditManager(MAX_LINES, DIRECTORY_PATH.toString());
        for (int i = 0; i < records; i++) {
            auditManager.addRecord("user" + (i + 1), LocalDateTime.now());
        }
        List<Path> filePaths;
        try (Stream<Path> sorted = Files.list(DIRECTORY_PATH).sorted()) {
            filePaths = sorted.toList();
        }
        int expectedFiles = records / MAX_LINES + (records % MAX_LINES == 0 ? 0 : 1);
        logger.info("Records: {} MAX_LINES: {} Files: {}",
                records, MAX_LINES, expectedFiles);
        Assertions.assertEquals(expectedFiles, filePaths.size());
    }

    @Order(3)
    @DisplayName("Тест: проверка количества записей в файле.")
    @RepeatedTest(value = 5, name = "Test {currentRepetition}. MaxLinesPerFile: {currentRepetition}. Records add: 5")
    void checkMaxLinesPerFileMaxLinesChanges(RepetitionInfo repetitionInfo) throws IOException {
        MAX_LINES = repetitionInfo.getCurrentRepetition();
        auditManager = new AuditManager(MAX_LINES, DIRECTORY_PATH.toString());
        int records = 5;
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
                expectedLines = records % MAX_LINES == 0 ? MAX_LINES : records % MAX_LINES;
            }
            logger.info("FileName: {}  ", path.getFileName());
            logger.info("FileContent: {}  ", listRecords);
            logger.info("FileSize expected: {} FileSize actual: {}  ",
                    expectedLines, listRecords.size());
            Assertions.assertEquals(expectedLines, listRecords.size());
        }
    }

    @Order(4)
    @DisplayName("Тест: проверка количества записей в файле.")
    @ParameterizedTest(name = "Test {index}. MaxLinesPerFile: 3. Records add: {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void checkMaxLinesPerFileCountRecordsChanges(int records) throws IOException {
        MAX_LINES = 3;
        auditManager = new AuditManager(MAX_LINES, DIRECTORY_PATH.toString());
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
                expectedLines = records % MAX_LINES == 0 ? MAX_LINES : records % MAX_LINES;
            }
            logger.info("FileName: {}  ", path.getFileName());
            logger.info("FileContent: {}  ", listRecords);
            logger.info("FileSize expected: {} FileSize actual: {}  ",
                    expectedLines, listRecords.size());
            Assertions.assertEquals(expectedLines, listRecords.size());
        }
    }

    @Order(5)
    @DisplayName("Тест: проверка имени файла.")
    @RepeatedTest(value = 5, name = "Test {currentRepetition}. MaxLinesPerFile: {currentRepetition}. Records add: 5")
    void checkFileNameMaxLinesChanges(RepetitionInfo repetitionInfo) throws IOException {
        MAX_LINES = repetitionInfo.getCurrentRepetition();
        auditManager = new AuditManager(MAX_LINES, DIRECTORY_PATH.toString());
        int records = 5;
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
            logger.info("ExpectedFileName: {} ActualFileName: {} ",
                    expectedFileName, actualFileName);
            Assertions.assertEquals(expectedFileName, actualFileName);
        }
    }

    @Order(6)
    @DisplayName("Тест: проверка имени файла.")
    @ParameterizedTest(name = "Test {index}. MaxLinesPerFile: 3. Records add: {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void checkFileNameCountRecordsChanges(int records) throws IOException {
        MAX_LINES = 3;
        auditManager = new AuditManager(MAX_LINES, DIRECTORY_PATH.toString());
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
            logger.info("Records add: {} ExpectedFileName: {} ActualFileName: {} ",
                    records, expectedFileName, actualFileName);
            Assertions.assertEquals(expectedFileName, actualFileName);
        }
    }

    @Order(7)
    @DisplayName("Тест: проверка записей в файле.")
    @RepeatedTest(value = 5, name = "Test {currentRepetition}. MaxLinesPerFile: {currentRepetition}. Records add: 5")
    void checkContentFileMaxLinesChanges(RepetitionInfo repetitionInfo) throws IOException {
        MAX_LINES = repetitionInfo.getCurrentRepetition();
        auditManager = new AuditManager(MAX_LINES, DIRECTORY_PATH.toString());
        int records = 5;
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

    @Order(8)
    @DisplayName("Тест: проверка записей в файле.")
    @ParameterizedTest(name = "Test {index}. MaxLinesPerFile: 3. Records add: {0}")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void checkContentFileCountRecordsChanges(int records) throws IOException {
        MAX_LINES = 3;
        auditManager = new AuditManager(MAX_LINES, DIRECTORY_PATH.toString());
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