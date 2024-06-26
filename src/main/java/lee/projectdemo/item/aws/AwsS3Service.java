package lee.projectdemo.item.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lee.projectdemo.item.item.image.Image;
import lee.projectdemo.item.item.ItemDto;
import lee.projectdemo.item.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지 파일 s3에 저장 후 Dto 리스트에 담아서 반환
    public List<Image> uploadFile(List<MultipartFile> multipartFile, String dirName) {
        List<Image> responseImage = new ArrayList<>();

        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 reponseDto에 추가
        multipartFile.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename(), dirName);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                log.info("a3 업로드 성공");
            } catch(IOException e) {
                log.error("파일 업로드에 실패했습니다.", e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

            // file에 관한 내용을 Dto로 변환 후 list에 담아 return
            responseImage.add(new Image(file.getOriginalFilename(), fileName));



        });

        return responseImage;
    }


    public void deleteFile(String fileName, String dirName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, dirName + "/" + fileName));
    }

    public void deleteFiles(List<Image> images, String dirName) {
        for (Image image : images) {
            String storeFileName = image.getStoreFileName();

            // Amazon S3에서 객체 삭제
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, dirName + "/" + storeFileName));
        }
    }

    // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 UUID를 붙여준다.
    private String createFileName(String fileName, String dirName) {
        return dirName + "/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였다.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    public String loadImage(String storedFileName) {
        return amazonS3.getUrl(bucket, storedFileName).toString();
    }

    public String loadEmptyImage(){
        return amazonS3.getUrl(bucket, "item/3985091d-b909-4d57-afb0-7bc9292b2b56.jpg").toString();
    }

    public Page<ItemDto> addImageItemDto(Page<ItemDto> itemDtos, Pageable pageable) {
        //페이징된 itemDtos를 그냥 ItemDto 리스트로 변형
        List<ItemDto> addImageDtos = itemDtos.getContent();
        for (ItemDto itemDto : addImageDtos) {
            List<String> imageUrls = new ArrayList<>();
            if(itemDto.getImages().size() == 0){
                //이미지가 없을 경우 대체이미지 삽입
                imageUrls.add(loadEmptyImage());
            }
            for (Image image : itemDto.getImages()) {
                imageUrls.add(loadImage(image.getStoreFileName()));
            }
            itemDto.setImageUrls(imageUrls);
        }

        return new PageImpl<>(addImageDtos, pageable , itemDtos.getTotalElements());
    }

    public List<MultipartFile> getImagesByItemId(Long itemId) {
        // itemId를 사용하여 해당 아이템의 이미지 파일명을 가져옴
        List<Image> images = imageRepository.findByItemId(itemId);

        List<String> storeFileNames = images.stream()
                .map(Image::getStoreFileName)
                .collect(Collectors.toList());

        System.out.println("드가자");
        System.out.println(storeFileNames);

        // 파일명을 사용하여 S3 스토리지에서 이미지 파일을 가져와 List<MultipartFile> 형태로 변환
        List<MultipartFile> multipartFiles = storeFileNames.stream()
                .map(fileName -> {
                    try {
                        // S3 스토리지에서 이미지 파일을 가져오기
                        S3Object s3Object = amazonS3.getObject(bucket, fileName);
                        S3ObjectInputStream inputStream = s3Object.getObjectContent();

                        System.out.println("므웨");
                        System.out.println(inputStream);

                        // 이미지 파일을 MultipartFile로 변환
                        return new MockMultipartFile("file", fileName,
                                s3Object.getObjectMetadata().getContentType(),
                                inputStream);
                    } catch (IOException e) {
                        log.error("이미지 파일을 가져오는 동안 오류가 발생했습니다.", e);
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 파일을 가져오는 동안 오류가 발생했습니다.");
                    }
                })
                .collect(Collectors.toList());

        return multipartFiles;
    }



}
