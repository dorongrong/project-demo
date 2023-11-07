package lee.projectdemo.item.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 774400231L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final BooleanPath bargain = createBoolean("bargain");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<lee.projectdemo.item.item.image.Image, lee.projectdemo.item.item.image.QImage> images = this.<lee.projectdemo.item.item.image.Image, lee.projectdemo.item.item.image.QImage>createList("images", lee.projectdemo.item.item.image.Image.class, lee.projectdemo.item.item.image.QImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> interestCount = createNumber("interestCount", Integer.class);

    public final ListPath<lee.projectdemo.item.item.interest.Interest, lee.projectdemo.item.item.interest.QInterest> interests = this.<lee.projectdemo.item.item.interest.Interest, lee.projectdemo.item.item.interest.QInterest>createList("interests", lee.projectdemo.item.item.interest.Interest.class, lee.projectdemo.item.item.interest.QInterest.class, PathInits.DIRECT2);

    public final StringPath itemName = createString("itemName");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath state = createString("state");

    public final lee.projectdemo.login.user.QUser user;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new lee.projectdemo.login.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

