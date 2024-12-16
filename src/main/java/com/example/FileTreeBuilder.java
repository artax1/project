package com.example;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * Класс для построения дерева файловой системы.
 */
public class FileTreeBuilder {
    private static final Logger logger = LogManager.getLogger(FileTreeBuilder.class);

    /**
     * Создаёт дерево файловой системы для отображения.
     *
     * @param rootDirectory корневая директория
     * @return дерево файлов (TreeView)
     */
    public static TreeView<String> buildFileTree(File rootDirectory) {
        try {
            TreeItem<String> rootItem = createFileNode(rootDirectory, 0);
            TreeView<String> treeView = new TreeView<>(rootItem);
            rootItem.setExpanded(true);
            return treeView;
        } catch (Exception e) {
            logger.error("Ошибка при построении дерева файлов для директории: {}", rootDirectory.getAbsolutePath(), e);
            return new TreeView<>(new TreeItem<>("Ошибка"));
        }
    }

    /**
     * Рекурсивно создаёт узлы дерева для файловой структуры.
     *
     * @param file текущий файл или директория
     * @param depth текущая глубина рекурсии
     * @return узел дерева
     */
    private static TreeItem<String> createFileNode(File file, int depth) {
        try {
            TreeItem<String> treeItem = new TreeItem<>(FileProcessor.formatFileDescription(file));
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    // Используем Stream API для фильтрации и добавления дочерних элементов
                    Arrays.stream(files)
                            .map(child -> createFileNode(child, depth + 1))
                            .forEach(treeItem.getChildren()::add);
                } else {
                    logger.warn("Недостаточно прав для доступа к директории: {} (Глубина: {})", file.getAbsolutePath(), depth);
                }
            }
            return treeItem;
        } catch (Exception e) {
            logger.error("Ошибка при создании узла дерева для файла: {} (Глубина: {})", file.getAbsolutePath(), depth, e);
            return new TreeItem<>(file.getName() + " [ERROR]");
        }
    }

    /**
     * Вычисляет общий размер директории, включая все вложенные файлы.
     *
     * @param directory директория
     * @return общий размер в байтах
     */
    public static long calculateDirectorySize(File directory) {
        try {
            if (!directory.isDirectory()) {
                return directory.length();
            }
            // Используем Stream API для суммирования размера файлов
            return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                    .mapToLong(FileTreeBuilder::calculateDirectorySize)
                    .sum();
        } catch (Exception e) {
            logger.error("Ошибка при расчёте размера директории: {}", directory.getAbsolutePath(), e);
            return 0;
        }
    }
}