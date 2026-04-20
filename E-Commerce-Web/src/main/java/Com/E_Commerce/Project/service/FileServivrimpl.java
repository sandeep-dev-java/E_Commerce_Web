package Com.E_Commerce.Project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServivrimpl implements FileService{
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //File names of current/original file
        String originalfile= file.getOriginalFilename();
        //generate a unique file name by using randomid
        String randomId= UUID.randomUUID().toString();
        String fileName= randomId.concat(originalfile.substring(originalfile.lastIndexOf('.')));
        String filepath= path+ File.separator+fileName;
        //check if path exist and if not then create
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        //upload the server
        Files.copy(file.getInputStream(), Paths.get(filepath));

        return fileName;
    }
}
