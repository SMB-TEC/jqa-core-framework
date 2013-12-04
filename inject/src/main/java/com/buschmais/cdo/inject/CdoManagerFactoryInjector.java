package com.buschmais.cdo.inject;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.buschmais.cdo.neo4j.impl.EmbeddedNeo4jCdoManagerFactoryImpl;
import com.google.inject.MembersInjector;

public class CdoManagerFactoryInjector<T> implements MembersInjector<T> {

    private Class<?>[] classes;
    private URL url;

    private Field field;

    public CdoManagerFactoryInjector(Field field, String url, Class<?>[] classes)
            throws MalformedURLException {
        this.field = field;
        this.classes = classes;
        // currently only embedded databases are supported
        this.url = (new File(url)).toURI().toURL();
        field.setAccessible(true);
    }

    public void injectMembers(T instance) {
        try {
            // currently only embedded databases are supported
            field.set(instance, new EmbeddedNeo4jCdoManagerFactoryImpl(new CdoUnit(null, null, url, null, new HashSet<>(Arrays.asList(classes)), CdoUnit.ValidationMode.AUTO, CdoUnit.TransactionAttribute.MANDATORY, new Properties())));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}