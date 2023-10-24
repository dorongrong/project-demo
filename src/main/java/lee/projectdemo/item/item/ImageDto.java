package lee.projectdemo.item.item;


import lombok.Data;

@Data
public class ImageDto {

    private String originalFileName;

    private String storeFileName;

    public ImageDto(String originalFileName, String storeFileName) {
        this.originalFileName = originalFileName;
        this.storeFileName = storeFileName;
    }
}
