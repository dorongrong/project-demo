package lee.projectdemo.item.item;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Image {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String url;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public Image(Long id, String url, String fileName, Item item) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.item = item;
    }

    public Image() {
    }
}
