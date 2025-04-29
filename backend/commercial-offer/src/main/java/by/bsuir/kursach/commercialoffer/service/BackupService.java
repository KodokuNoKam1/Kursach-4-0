package by.bsuir.kursach.commercialoffer.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BackupService {

    public byte[] createBackup() throws Exception {
        File dbFile = new File("D:/Kursach-4-0/backend/commercial_offer.db");
        String backupFileName = "backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".db";
        File backupFile = new File("D:/Kursach-4-0/backend/backups/" + backupFileName);
        backupFile.getParentFile().mkdirs();
        Files.copy(dbFile.toPath(), backupFile.toPath());
        return Files.readAllBytes(backupFile.toPath());
    }
}