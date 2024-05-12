package com.ibrahimokic.ordermanagement.mapper;

import java.util.Collections;
import java.util.List;

public interface Mapper<A, B> {
    B mapTo(A a);

    A mapFrom(B b);

    default List<A> mapListToEntityList(List<B> b) {
        return Collections.emptyList();
    }
}
