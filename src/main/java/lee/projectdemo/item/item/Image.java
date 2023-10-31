package lee.projectdemo.item.item;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String originalFileName;

    private String storeFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public Image(Long id, String originalFileName, String storeFileName, Item item) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.storeFileName = storeFileName;
        this.item = item;
    }

    public Image(String originalFileName, String storeFileName, Item item) {
        this.originalFileName = originalFileName;
        this.storeFileName = storeFileName;
        this.item = item;
    }

    public Image(String originalFileName, String storeFileName) {
        this.originalFileName = originalFileName;
        this.storeFileName = storeFileName;
    }

    public Image() {
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", originalFileName='" + originalFileName + '\'' +
                ", storeFileName='" + storeFileName + '\'' +
                '}';
    }

    public void changeItem(Item item) {
        this.item = item;
    }
}
