package by.bsuir.kursach.commercialoffer.controller;

import by.bsuir.kursach.commercialoffer.service.BackupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backup")
@Tag(name = "Backup", description = "API for database backup")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @GetMapping
    @Operation(summary = "Create database backup", description = "Creates and downloads a backup of the database (ADMIN only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> createBackup() throws Exception {
        byte[] backup = backupService.createBackup();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=backup.db")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(backup);
    }
}