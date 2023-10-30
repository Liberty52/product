package com.liberty52.product.service.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductOption is a Querydsl query type for ProductOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductOption extends EntityPathBase<ProductOption> {

    private static final long serialVersionUID = -241302920L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductOption productOption = new QProductOption("productOption");

    public final StringPath id = createString("id");

    public final StringPath name = createString("name");

    public final BooleanPath onSale = createBoolean("onSale");

    public final ListPath<OptionDetail, QOptionDetail> optionDetails = this.<OptionDetail, QOptionDetail>createList("optionDetails", OptionDetail.class, QOptionDetail.class, PathInits.DIRECT2);

    public final QProduct product;

    public final BooleanPath require = createBoolean("require");

    public QProductOption(String variable) {
        this(ProductOption.class, forVariable(variable), INITS);
    }

    public QProductOption(Path<? extends ProductOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductOption(PathMetadata metadata, PathInits inits) {
        this(ProductOption.class, metadata, inits);
    }

    public QProductOption(Class<? extends ProductOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

