package com.scaling.libraryservice.mapBook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTimeStamp is a Querydsl query type for TimeStamp
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QTimeStamp extends EntityPathBase<TimeStamp> {

    private static final long serialVersionUID = -2135812876L;

    public static final QTimeStamp timeStamp = new QTimeStamp("timeStamp");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> modifiedAt = createDate("modifiedAt", java.time.LocalDate.class);

    public QTimeStamp(String variable) {
        super(TimeStamp.class, forVariable(variable));
    }

    public QTimeStamp(Path<? extends TimeStamp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTimeStamp(PathMetadata metadata) {
        super(TimeStamp.class, metadata);
    }

}

