package com.gennlife.dao;

import com.gennlife.entity.CustomActiveIndex;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by liuzhen.
 * Date: 2017/10/10
 * Time: 10:58
 */
public interface CustomActiveIndexDao extends CrudRepository<CustomActiveIndex,Long> {
    CustomActiveIndex findById(Long id);
    Iterable<CustomActiveIndex> findAll();
}
