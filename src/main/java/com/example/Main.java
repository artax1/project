package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Главный класс приложения File System Analyzer.
 * <p>
 * Отвечает за запуск JavaFX-приложения, выбор директории
 * и отображение файловой структуры в виде дерева.
 */
public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * Запуск приложения.
     *
     * @param primaryStage главное окно приложения
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Программа File System Analyzer запущена.");
            primaryStage.setTitle("File System Analyzer");

            BorderPane mainLayout = new BorderPane();
            File selectedDirectory = selectDirectory(primaryStage);

            if (selectedDirectory == null) {
                logger.warn("Директория не выбрана, приложение завершено.");
                primaryStage.close();
                return;
            }

            logger.info("Выбрана директория: {}", selectedDirectory.getAbsolutePath());

            TreeView<String> fileTreeView = FileTreeBuilder.buildFileTree(selectedDirectory);
            mainLayout.setCenter(fileTreeView);

            long totalSize = FileTreeBuilder.calculateDirectorySize(selectedDirectory);
            logger.info("Общий вес директории: {}", FileProcessor.formatFileSize(totalSize));

            Scene scene = new Scene(mainLayout, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            logger.error("Ошибка при запуске приложения.", e);
            primaryStage.close();
        }
    }

    /**
     * Отображает диалог выбора директории.
     *
     * @param stage главное окно приложения
     * @return выбранная директория или {@code null}, если директория не выбрана
     */
    private File selectDirectory(Stage stage) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Выберите директорию для анализа");
            return directoryChooser.showDialog(stage);
        } catch (Exception e) {
            logger.error("Ошибка при выборе директории.", e);
            return null;
        }
    }

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        logger.info("Приложение запущено.");
        launch(args);
    }
}