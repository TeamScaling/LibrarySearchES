package com.scaling.libraryservice.mapBook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLibraryHasBook is a Querydsl query type for LibraryHasBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLibraryHasBook extends EntityPathBase<LibraryHasBook> {

    private static final long serialVersionUID = 1142034986L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLibraryHasBook libraryHasBook = new QLibraryHasBook("libraryHasBook");

    public final NumberPath<Integer> areaCd = createNumber("areaCd", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> isbn13 = createNumber("isbn13", Double.class);

    public final QLibrary library;

    public final NumberPath<Integer> loanCnt = createNumber("loanCnt", Integer.class);

    public final DatePath<java.time.LocalDate> regisDate = createDate("regisDate", java.time.LocalDate.class);

    public QLibraryHasBook(String variable) {
        this(LibraryHasBook.class, forVariable(variable), INITS);
    }

    public QLibraryHasBook(Path<? extends LibraryHasBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLibraryHasBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLibraryHasBook(PathMetadata metadata, PathInits inits) {
        this(LibraryHasBook.class, metadata, inits);
    }

    public QLibraryHasBook(Class<? extends LibraryHasBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.library = inits.isInitialized("library") ? new QLibrary(forProperty("library")) : null;
    }

}

