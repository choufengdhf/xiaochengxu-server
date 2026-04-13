package edu.platform.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/files")
public class FileController {

  @Value("${app.uploadDir}")
  private String uploadDir;

  private static final Map<Long, Map<String, Object>> FILES = new HashMap<>();
  private static long FILE_ID = 1;

  @PostMapping("/upload")
  public Map<String, Object> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam(value="biz", required=false) String biz,
                                    @RequestParam(value="filename", required=false) String filename) throws Exception {
    Files.createDirectories(Paths.get(uploadDir));
    long id = FILE_ID++;

    String original = (filename != null && !filename.isBlank()) ? filename : file.getOriginalFilename();
    String safe = (id + "_" + (original == null ? "file" : original)).replaceAll("[\\\\/]+", "_");
    Path target = Paths.get(uploadDir).resolve(safe);

    try (InputStream in = file.getInputStream()) {
      Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
    }

    Map<String, Object> meta = new HashMap<>();
    meta.put("id", id);
    meta.put("path", target.toString());
    meta.put("filename", original);
    meta.put("biz", biz == null ? "" : biz);
    FILES.put(id, meta);

    return Map.of("id", id, "filename", original);
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<byte[]> download(@PathVariable long id) throws Exception {
    Map<String, Object> meta = FILES.get(id);
    if (meta == null) return ResponseEntity.notFound().build();

    Path path = Paths.get((String) meta.get("path"));
    byte[] bytes;
    try (InputStream in = Files.newInputStream(path)) {
      bytes = StreamUtils.copyToByteArray(in);
    }

    String filename = (String) meta.get("filename");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDisposition(ContentDisposition.attachment().filename(filename == null ? "file" : filename).build());
    return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
  }
}