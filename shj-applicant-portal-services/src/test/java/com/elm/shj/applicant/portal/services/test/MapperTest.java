/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.test;

import com.elm.dcc.foundation.commons.core.mapper.CycleAvoidingMappingContext;
import com.elm.dcc.foundation.commons.core.mapper.IGenericMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Generic test for mappers using {@link IGenericMapper}.
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
@ExtendWith(MockitoExtension.class)
public abstract class MapperTest<D, E, M extends IGenericMapper<D, E>> {

    IGenericMapper<D, E> mapperToTest;

    private D dtoInstance;
    private E entityInstance;
    private E mapperClass;

    private SortedMap<String, GetterSetterPair> dtoGetterSetterMapping = new TreeMap<>();
    private SortedMap<String, GetterSetterPair> entityGetterSetterMapping = new TreeMap<>();

    @Mock
    private CycleAvoidingMappingContext mappingContext;

    /**
     * Returns the class of the mapper to test
     *
     * @return The class of the mapper to test
     */
    protected abstract Class<M> getMapperClass();

    /**
     * Returns an instance of the Dto class
     *
     * @return An instance of the Dto class
     */
    protected abstract D getDtoInstance();

    /**
     * Returns an instance of the Entity class
     *
     * @return An instance of the Entity class
     */
    protected abstract E getEntityInstance();

    @BeforeEach
    private void setUp() throws Exception {
        mapperToTest = Mappers.getMapper(getMapperClass());
        dtoInstance = loadGettersAndSetters(getDtoInstance(), dtoGetterSetterMapping);
        entityInstance = loadGettersAndSetters(getEntityInstance(), entityGetterSetterMapping);
    }

    @Test
    public void test_to_entity() throws Exception {
        E toEntity = mapperToTest.toEntity(dtoInstance, mappingContext);
        assertEntityPropertiesEqualDtoOnes(toEntity);
    }

    @Test
    public void test_from_entity() throws Exception {
        D toDto = mapperToTest.fromEntity(entityInstance, mappingContext);
        assertDtoPropertiesEqualEntityOnes(toDto);
    }

    @Test
    public void test_to_entity_list() throws Exception {
        List<E> toEntityList = new ArrayList<>();
        toEntityList.add(mapperToTest.toEntity(dtoInstance, mappingContext));
        toEntityList.add(mapperToTest.toEntity(dtoInstance, mappingContext));

        for (E e : toEntityList) {
            assertEntityPropertiesEqualDtoOnes(e);
        }
    }

    @Test
    public void test_from_entity_list() throws Exception {
        List<D> fromEntityList = new ArrayList<>();
        fromEntityList.add(mapperToTest.fromEntity(entityInstance, mappingContext));
        fromEntityList.add(mapperToTest.fromEntity(entityInstance, mappingContext));

        for (D d : fromEntityList) {
            assertDtoPropertiesEqualEntityOnes(d);
        }
    }

    private void assertEntityPropertiesEqualDtoOnes(E toEntity) throws Exception {
        for (final Map.Entry<String, GetterSetterPair> entry : dtoGetterSetterMapping.entrySet()) {
            final GetterSetterPair dtoPair = entry.getValue();
            final String fieldName = entry.getKey();
            if (!"Class".equals(fieldName)) {
                final GetterSetterPair entityPair = entityGetterSetterMapping.get(fieldName);
                if (entityPair != null && entityPair.getGetter() != null && dtoPair != null && dtoPair.getGetter() != null) {
                    Object dtoFieldValue = dtoPair.getGetter().invoke(dtoInstance);
                    Object entityFieldValue = entityPair.getGetter().invoke(toEntity);
                    assertEquals(dtoFieldValue, entityFieldValue);
                }
            }
        }
    }

    private void assertDtoPropertiesEqualEntityOnes(D toDto) throws Exception {
        for (final Map.Entry<String, GetterSetterPair> entry : entityGetterSetterMapping.entrySet()) {
            final GetterSetterPair entityPair = entry.getValue();
            final String fieldName = entry.getKey();
            if (!"Class".equals(fieldName)) {
                final GetterSetterPair dtoPair = dtoGetterSetterMapping.get(fieldName);
                if (entityPair != null && entityPair.getGetter() != null && dtoPair != null && dtoPair.getGetter() != null) {
                    Object entityFieldValue = entityPair.getGetter().invoke(entityInstance);
                    Object dtoFieldValue = dtoPair.getGetter().invoke(toDto);
                    assertEquals(entityFieldValue, dtoFieldValue);
                }
            }
        }
    }

    private <T> T loadGettersAndSetters(T instance, SortedMap<String, GetterSetterPair> getterSetterMapping) throws Exception {
        getterSetterMapping.clear();
        /* Sort items for consistent test runs. */

        for (final Method method : instance.getClass().getMethods()) {
            final String methodName = method.getName();

            String objectName;
            if (methodName.startsWith("get") && method.getParameters().length == 0) {
                /* Found the get method. */
                objectName = methodName.substring("get".length());

                GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                if (getterSettingPair == null) {
                    getterSettingPair = new GetterSetterPair();
                    getterSetterMapping.put(objectName, getterSettingPair);
                }
                getterSettingPair.setGetter(method);
            } else if (methodName.startsWith("set") && method.getParameters().length == 1) {
                /* Found the set method. */
                objectName = methodName.substring("set".length());

                GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                if (getterSettingPair == null) {
                    getterSettingPair = new GetterSetterPair();
                    getterSetterMapping.put(objectName, getterSettingPair);
                }
                getterSettingPair.setSetter(method);
            } else if (methodName.startsWith("is") && method.getParameters().length == 0) {
                /* Found the is method, which really is a get method. */
                objectName = methodName.substring("is".length());

                GetterSetterPair getterSettingPair = getterSetterMapping.get(objectName);
                if (getterSettingPair == null) {
                    getterSettingPair = new GetterSetterPair();
                    getterSetterMapping.put(objectName, getterSettingPair);
                }
                getterSettingPair.setGetter(method);
            }
        }

        /*
         * Found all our mappings. Now call the getter and setter or set the field via reflection and call the getting
         * it doesn't have a setter.
         */
        for (final Map.Entry<String, GetterSetterPair> entry : getterSetterMapping.entrySet()) {
            final GetterSetterPair pair = entry.getValue();
            if (pair.hasGetterAndSetter()) {
                Class<?> parameterType = pair.getSetter().getParameterTypes()[0];
                try {
                    pair.getSetter().invoke(instance, generateRandomValue(parameterType));
                } catch (java.lang.IllegalArgumentException e) {
                    System.out.println("ex");
                }
            }
        }

        return instance;
    }

    private <T> T generateRandomValue(Class<T> clazz) throws Exception {
        if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
            return (T) Boolean.TRUE;
        } else if (clazz.equals(Date.class)) {
            return (T) new Date();
        } else if (clazz.equals(String.class)) {
            return (T) "Random Val";
        } else if (clazz.equals(int.class)
                || clazz.equals(long.class)
                || clazz.equals(short.class)
                || clazz.equals(float.class)
                || clazz.equals(double.class)
                || Number.class.isAssignableFrom(clazz)) {
            return (T) getRandomNum((Class<Number>) clazz);
        } else {
            return null;
        }
    }

    private static <N extends Number> N getRandomNum(Class<N> eClass)
            throws Exception {

        double random = (Math.random() * 200 + 1);

        if (eClass.equals(int.class)
                || eClass.equals(long.class)
                || eClass.equals(short.class)
                || eClass.equals(float.class)
                || eClass.equals(double.class)) {
            eClass = (Class<N>) Integer.class;
        }

        Constructor<N> eConstructor = eClass.getConstructor(String.class);

        N eInstance = null;

        try {
            eInstance = eConstructor.newInstance("" + random);
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof NumberFormatException) {
                eInstance = eConstructor.newInstance("" + (int) random);
            } else {
                throw ex;
            }
        }

        return eInstance;

    }


    /**
     * A utility class which holds a related getter and setter method.
     */
    private static class GetterSetterPair {
        /**
         * The get method.
         */
        private Method getter;

        /**
         * The set method.
         */
        private Method setter;

        /**
         * Returns the get method.
         *
         * @return The get method.
         */
        public Method getGetter() {
            return getter;
        }

        /**
         * Returns the set method.
         *
         * @return The set method.
         */
        public Method getSetter() {
            return setter;
        }

        /**
         * Returns if this has a getter and setting method set.
         *
         * @return If this has a getter and setting method set.
         */
        public boolean hasGetterAndSetter() {
            return this.getter != null && this.setter != null;
        }

        /**
         * Sets the get Method.
         *
         * @param getter The get Method.
         */
        public void setGetter(Method getter) {
            this.getter = getter;
        }

        /**
         * Sets the set Method.
         *
         * @param setter The set Method.
         */
        public void setSetter(Method setter) {
            this.setter = setter;
        }
    }

}
