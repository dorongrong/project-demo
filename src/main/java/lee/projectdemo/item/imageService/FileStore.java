package lee.projectdemo.item.imageService;

import lee.projectdemo.item.item.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//FileStore은 다중 이미지인 MultipartFile을 ImageDto로 변형 하는 역할을 함
@Component
@Slf4j
public class FileStore {

//    @Value("${file.dir}")
    private String fileDir;
    public String getFullPath(String filename) {
        return fileDir + filename;
    }
    public List<Image> storeFiles(List<MultipartFile> multipartFiles)
            throws IOException {
        List<Image> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }
    public Image storeFile(MultipartFile multipartFile)
    {
        //예외 처리 해야함
        try {
            if (multipartFile.isEmpty()) {
                return null;
            }
            String originalFilename = multipartFile.getOriginalFilename();
            String storeFileName = createStoreFileName(originalFilename);
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
            return new Image(originalFilename, storeFileName);
        } catch(IOException e) {
            log.error("파일을 변환하는데 있어 예외가 발생했습니다.");
                return null;
        }
    }
    
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}

