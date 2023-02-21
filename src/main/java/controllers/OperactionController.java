package controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.FileService;
import services.OperactionService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/operactions")
public class OperactionController {

    private OperactionService operactionService;

    private final FileService operactionFileService;


    public OperactionController(OperactionService operactionService,@Qualifier("operationsFileServiceImpl") FileService operactionFileService) {
        this.operactionService = operactionService;
        this.operactionFileService = operactionFileService;
    }
    @GetMapping("/createTextFile")
    public ResponseEntity<Object> createTempTextFileWithRecipes() {
        File file = operactionService.getTextFile();
        try {
            if (file == null) {
                return ResponseEntity.notFound().build();
            }
            if (Files.size(file.toPath()) == 0) {
                return ResponseEntity.noContent().build();
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"operationsText.txt\"")
                    .contentLength(Files.size(file.toPath()))
                    .body(resource);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }
}


