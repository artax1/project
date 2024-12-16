import com.example.FileProcessor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Тестовый класс для проверки функциональности метода формирования описания файлов и директорий.
 * Этот класс проверяет правильность работы метода {@link FileProcessor#formatFileDescription(File)},
 * который генерирует описание для различных типов файлов и директорий.
 * Тестируются следующие случаи:
 * <ul>
 *   <li>Обычные файлы</li>
 *   <li>Пустые файлы</li>
 *   <li>Пустые директории</li>
 *   <li>Директории с вложенными файлами</li>
 * </ul>
 */
public class FileProcessorTest {

    /**
     * Тестирует метод {@link FileProcessor#formatFileDescription(File)} для различных типов файлов и директорий.
     * В данном тесте проверяется, что метод корректно генерирует описание для:
     * <ul>
     *   <li>Обычных файлов</li>
     *   <li>Пустых файлов</li>
     *   <li>Пустых директорий</li>
     *   <li>Директорий с вложенными файлами</li>
     * </ul>
     *
     * @throws IOException Если произошла ошибка при создании файлов или записи в них.
     */
    @Test
    public void testFormatFileDescription() throws IOException {
        // Создаем временные файлы
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit(); // Удалить файл по завершению теста
        Files.write(tempFile.toPath(), "Hello, World!".getBytes());

        // Пустой файл
        File emptyFile = File.createTempFile("emptyFile", ".txt");
        emptyFile.deleteOnExit(); // Удалить файл по завершению теста

        // Пустая директория
        File tempDir = Files.createTempDirectory("testDir").toFile();
        tempDir.deleteOnExit(); // Удалить директорию по завершению теста

        // Директория с файлами
        File nestedDir = Files.createTempDirectory(tempDir.toPath(), "nestedDir").toFile();
        nestedDir.deleteOnExit(); // Удалить вложенную директорию по завершению теста
        File nestedFile = new File(nestedDir, "nestedFile.txt");
        Files.write(nestedFile.toPath(), "Nested file content".getBytes());

        // Тестируем метод для обычного файла
        String fileDescription = FileProcessor.formatFileDescription(tempFile);
        assertNotNull(fileDescription);
        assertTrue(fileDescription.contains(tempFile.getName()));

        // Тестируем метод для пустого файла
        String emptyFileDescription = FileProcessor.formatFileDescription(emptyFile);
        assertNotNull(emptyFileDescription);
        assertTrue(emptyFileDescription.contains(emptyFile.getName()));

        // Тестируем метод для пустой директории
        String dirDescription = FileProcessor.formatFileDescription(tempDir);
        assertNotNull(dirDescription);
        assertTrue(dirDescription.contains(tempDir.getName()));
        assertTrue(dirDescription.contains("[DIR]"));

        // Тестируем метод для директории с вложенными файлами
        String nestedDirDescription = FileProcessor.formatFileDescription(nestedDir);
        assertNotNull(nestedDirDescription);
        assertTrue(nestedDirDescription.contains(nestedDir.getName()));

        // Проверяем описание вложенного файла
        String nestedFileDescription = FileProcessor.formatFileDescription(nestedFile);
        assertNotNull(nestedFileDescription);
        assertTrue(nestedFileDescription.contains(nestedFile.getName()));
    }
}
