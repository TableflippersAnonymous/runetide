package com.runetide.services.internal.xp.server.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.runetide.common.BaseDao;
import com.runetide.services.internal.xp.common.XP;
import com.runetide.services.internal.xp.common.XPRef;
import com.runetide.services.internal.xp.server.dto.XPByParent;

import java.util.UUID;

@Dao
public interface XPDao extends BaseDao<XP> {
    @Select
    XP get(UUID id);

    @Select
    PagingIterable<XPByParent> getByParent(UUID id);

    default XP get(XPRef ref) {
        return get(ref.getId());
    }

    default PagingIterable<XPByParent> getByParent(XPRef ref) {
        return getByParent(ref.getId());
    }
}
