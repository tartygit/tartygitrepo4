package com.cth.sdm.service;

import com.cth.sdm.handler.DocumentHandler;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FolderWatcherService {

    private static final Logger log = LoggerFactory.getLogger(FolderWatcherService.class);

    @Value("${app.watched-folder:./incoming_documents}")
    private String watchedFolderPath;

    @Value("${app.processed-folder:./processed_documents}")
    private String processedFolderPath;

    private final List<DocumentHandler> handlers;

    public FolderWatcherService(List<DocumentHandler> handlers) {
        this.handlers = handlers;
    }

    @PostConstruct
    public void initFolders() {
        try {
            File watchedDir = new File(watchedFolderPath);
            if (!watchedDir.exists()) {
                watchedDir.mkdirs();
            }
            File processedDir = new File(processedFolderPath);
            if (!processedDir.exists()) {
                processedDir.mkdirs();
            }
            log.info("Folder Watcher initialized for directory: {}", watchedDir.getAbsolutePath());
        } catch (Exception e) {
            log.error("Failed to initialize watched folders: {}", e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 5000) // Scan every 5 seconds
    public void scanDirectory() {
        File folder = new File(watchedFolderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            if (file.isFile() && !file.getName().startsWith(".")) {
                log.info("Folder Watcher detected new file: {}", file.getName());
                for (DocumentHandler handler : handlers) {
                    if (handler.supports(file)) {
                        try {
                            handler.process(file);
                            // Move file to processed folder
                            File dest = new File(processedFolderPath, file.getName());
                            Files.move(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            log.info("Moved processed file {} to {}", file.getName(), dest.getAbsolutePath());
                            break;
                        } catch (Exception e) {
                            log.error("Failed to handle file {}: {}", file.getName(), e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
