package com.runetide.common;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.SetEntity;

public interface BaseDao<T> {
    @Insert
    void save(T t);

    @Delete
    void delete(T t);

    @SetEntity
    void bind(T t, BoundStatementBuilder builder);

    @Select
    PagingIterable<T> all();
}
