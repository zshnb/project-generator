package ${packageName};

import ${commonPackageName}.Response;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@RestController
public class StaticResourceController {
    private final Path path = Paths.get("upload");

    @PostMapping("/upload")
    public Response<String> upload(@RequestParam MultipartFile file) {
        try {
            if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                Files.createDirectory(path);
            }

            Files.copy(file.getInputStream(), this.path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file", e);
        }
        return Response.ok("http://localhost:8081/download?filename=" + file.getOriginalFilename());
    }

    @GetMapping("/download")
    public Resource download(@RequestParam String filename) {
        Path file = path.resolve(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            } else {
                throw new RuntimeException("Could not read the file.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
