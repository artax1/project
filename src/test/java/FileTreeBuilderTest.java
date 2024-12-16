import com.example.FileTreeBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Тестовый класс для проверки функциональности метода расчета размера директорий.
 * Этот класс проверяет правильность работы метода {@link FileTreeBuilder#calculateDirectorySize(File)},
 * который рассчитывает общий размер всех файлов внутри директории, включая вложенные файлы.
 * Тестируются следующие случаи:
 * <ul>
 *   <li>Пустая директория</li>
 *   <li>Директория с одним файлом</li>
 *   <li>Директория с несколькими файлами</li>
 * </ul>
 */
public class FileTreeBuilderTest {

    /**
     * Тестирует метод {@link FileTreeBuilder#calculateDirectorySize(File)} для различных типов директорий.
     * В данном тесте проверяется корректность расчета размера директории для:
     * <ul>
     *   <li>Пустых директорий</li>
     *   <li>Директорий с одним файлом</li>
     *   <li>Директорий с несколькими файлами</li>
     * </ul>
     *
     * @throws IOException Если произошла ошибка при создании файлов или директорий.
     */
    @Test
    public void testCalculateDirectorySize() throws IOException {
        // Создаем временные файлы и директории
        File tempDir = Files.createTempDirectory("testDir").toFile();
        tempDir.deleteOnExit(); // Удалить директорию по завершению теста

        // Пустая директория
        long emptyDirSize = FileTreeBuilder.calculateDirectorySize(tempDir);
        assertEquals(0, emptyDirSize); // Размер пустой директории должен быть 0

        // Директория с одним файлом
        File tempFile = new File(tempDir, "file1.txt");
        Files.write(tempFile.toPath(), "Hello, World!".getBytes());
        long dirWithFileSize = FileTreeBuilder.calculateDirectorySize(tempDir);
        assertTrue(dirWithFileSize > 0); // Размер директории с файлом должен быть больше 0

        // Директория с несколькими файлами
        File tempFile2 = new File(tempDir, "file2.txt");
        Files.write(tempFile2.toPath(), "More content".getBytes());
        long dirWithMultipleFilesSize = FileTreeBuilder.calculateDirectorySize(tempDir);
        assertTrue(dirWithMultipleFilesSize > dirWithFileSize); // Размер директории с несколькими файлами должен быть больше
    }
}