package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;

/**
 * Класс для обработки и форматирования информации о файлах.
 */
public class FileProcessor {
    private static final Logger logger = LogManager.getLogger(FileProcessor.class);

    /**
     * Формирует описание файла: имя, размер, дата создания и владелец.
     *
     * @param file файл или директория
     * @return описание файла в строковом формате
     */
    public static String formatFileDescription(File file) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            String size = file.isDirectory() ? "[DIR]" : formatFileSize(attributes.size());
            String creationDate = formatCreationDate(attributes.creationTime().toMillis());
            String owner = getFileOwner(file); // Получаем владельца
            return String.format("%s | %s | Created: %s | Owner: %s", file.getName(), size, creationDate, owner);
        } catch (Exception e) {
            logger.error("Ошибка при получении атрибутов для файла: {}", file.getAbsolutePath(), e);
            return file.getName() + " [ERROR]";
        }
    }

    /**
     * Получает владельца файла или директории.
     *
     * @param file файл или директория
     * @return имя владельца
     */
    public static String getFileOwner(File file) {
        try {
            UserPrincipal owner = Files.getOwner(file.toPath());
            return owner.getName(); // Имя владельца
        } catch (Exception e) {
            logger.error("Ошибка при получении владельца для файла: {}", file.getAbsolutePath(), e);
            return "Unknown Owner"; // Если не удалось получить владельца
        }
    }

    /**
     * Форматирует размер файла в удобочитаемый формат.
     *
     * @param size размер файла в байтах
     * @return строка с отформатированным размером (например, KB, MB, GB)
     */
    public static String formatFileSize(long size) {
        if (size >= 1 << 30) return String.format("%.2f GB", size / (double) (1 << 30));
        if (size >= 1 << 20) return String.format("%.2f MB", size / (double) (1 << 20));
        if (size >= 1 << 10) return String.format("%.2f KB", size / (double) (1 << 10));
        return size + " B";
    }

    /**
     * Форматирует дату создания файла.
     *
     * @param creationTimeMillis время создания файла в миллисекундах
     * @return строка с отформатированной датой
     */
    public static String formatCreationDate(long creationTimeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(creationTimeMillis);
    }
}
