package com.scaling.libraryservice.mapBook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLibrary is a Querydsl query type for Library
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLibrary extends EntityPathBase<Library> {

    private static final long serialVersionUID = 1102506041L;

    public static final QLibrary library = new QLibrary("library");

    public final NumberPath<Integer> areaCd = createNumber("areaCd", Integer.class);

    public final StringPath libArea = createString("libArea");

    public final NumberPath<Double> libLat = createNumber("libLat", Double.class);

    public final NumberPath<Double> libLon = createNumber("libLon", Double.class);

    public final StringPath libNm = createString("libNm");

    public final NumberPath<Integer> libNo = createNumber("libNo", Integer.class);

    public final StringPath libUrl = createString("libUrl");

    public final StringPath oneAreaNm = createString("oneAreaNm");

    public final StringPath twoAreaNm = createString("twoAreaNm");

    public QLibrary(String variable) {
        super(Library.class, forVariable(variable));
    }

    public QLibrary(Path<? extends Library> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLibrary(PathMetadata metadata) {
        super(Library.class, metadata);
    }

}

