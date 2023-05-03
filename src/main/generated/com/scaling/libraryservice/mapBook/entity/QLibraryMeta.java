package com.scaling.libraryservice.mapBook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLibraryMeta is a Querydsl query type for LibraryMeta
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLibraryMeta extends EntityPathBase<LibraryMeta> {

    private static final long serialVersionUID = 1061858782L;

    public static final QLibraryMeta libraryMeta = new QLibraryMeta("libraryMeta");

    public final NumberPath<Integer> areaCd = createNumber("areaCd", Integer.class);

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath oneArea = createString("oneArea");

    public final StringPath twoArea = createString("twoArea");

    public QLibraryMeta(String variable) {
        super(LibraryMeta.class, forVariable(variable));
    }

    public QLibraryMeta(Path<? extends LibraryMeta> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLibraryMeta(PathMetadata metadata) {
        super(LibraryMeta.class, metadata);
    }

}

