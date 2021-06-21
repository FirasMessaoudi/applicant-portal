/*
 *  Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.generic;

import com.elm.dcc.foundation.commons.core.mapper.CycleAvoidingMappingContext;
import com.elm.dcc.foundation.commons.core.mapper.IGenericMapper;
import com.elm.dcc.foundation.commons.core.mapper.MapperRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for all services with repository lookup capability
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public class GenericService<T, D, K extends Serializable> {

    @Autowired
    private JpaRepository<T, K> repository;

    @Autowired
    private MapperRegistry mapperRegistry;

    @Autowired
    protected CycleAvoidingMappingContext mappingContext;

    protected Class<T> entityClass;

    protected Class<D> dtoClass;

    @SuppressWarnings("unchecked")
    public GenericService() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        this.dtoClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
    }

    @Transactional
    public D findOne(K id) {
        return getMapper().fromEntity(getRepository().findById(id).orElse(null), mappingContext);
    }

    @Transactional
    public List<D> findAll() {
        return mapList(getRepository().findAll());
    }

    public List<D> mapList(List<T> objects) {
        final List<D> mappedList = new ArrayList<>();
        for (T o : objects) {
            mappedList.add(getMapper().fromEntity(o, mappingContext));
        }
        return mappedList;
    }

    public Page<D> mapPage(Page<T> objectsPage) {
        return objectsPage == null ? null : objectsPage.map(source -> getMapper().fromEntity(source, mappingContext));
    }

    public D save(D dto) {
        return getMapper().fromEntity(getRepository().saveAndFlush(getMapper().toEntity(dto, mappingContext)), mappingContext);
    }

    public void saveAll(List<D> dtoList) {
        final List<T> mappedList = new ArrayList<>();
        for (D o : dtoList) {
            mappedList.add(getMapper().toEntity(o, mappingContext));
        }
        getRepository().saveAll(mappedList);
    }

    public JpaRepository<T, K> getRepository() {
        return repository;
    }

    public IGenericMapper<D, T> getMapper() {
        return mapperRegistry.mapperOf(dtoClass, entityClass);
    }

}
