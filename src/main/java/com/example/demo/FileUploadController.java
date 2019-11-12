package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Geonguk Han
 * @since 2019-11-12
 */
@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) {
        List<String> serveFile = storageService.loadAll().map(
                // todo: MvcUriComponentsBuilder
                path -> MvcUriComponentsBuilder.
                        fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString()).build().toString()
        ).collect(Collectors.toList());

        model.addAttribute("files", serveFile);

        return "uploadForm";
    }

    // todo :정규 표현식
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    // todo: resource
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        // todo: content_disposition
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Model model) {
        storageService.store(file);
        // todo: what is flashAttribute
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded" + file.getOriginalFilename() + "!");
        model.addAttribute("message1", "hello world");
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException e){
        return ResponseEntity.notFound().build();
    }
}
