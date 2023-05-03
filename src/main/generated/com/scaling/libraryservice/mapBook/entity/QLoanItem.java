package com.scaling.libraryservice.mapBook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLoanItem is a Querydsl query type for LoanItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoanItem extends EntityPathBase<LoanItem> {

    private static final long serialVersionUID = 814966885L;

    public static final QLoanItem loanItem = new QLoanItem("loanItem");

    public final QTimeStamp _super = new QTimeStamp(this);

    public final StringPath classNo = createString("classNo");

    //inherited
    public final DatePath<java.time.LocalDate> createdAt = _super.createdAt;

    public final NumberPath<Double> isbn13 = createNumber("isbn13", Double.class);

    public final NumberPath<Integer> loan_count = createNumber("loan_count", Integer.class);

    //inherited
    public final DatePath<java.time.LocalDate> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> no = createNumber("no", Long.class);

    public final NumberPath<Integer> ranking = createNumber("ranking", Integer.class);

    public QLoanItem(String variable) {
        super(LoanItem.class, forVariable(variable));
    }

    public QLoanItem(Path<? extends LoanItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLoanItem(PathMetadata metadata) {
        super(LoanItem.class, metadata);
    }

}

