package com.buschmais.cdo.neo4j.test.implementedby.composite;

import com.buschmais.cdo.neo4j.api.proxy.NodeProxyMethod;
import org.neo4j.graphdb.Node;

public class IncrementValueMethod implements NodeProxyMethod {

    @Override
    public Object invoke(Node node, Object instance, Object[] args) {
        A a = A.class.cast(instance);
        int value = a.getValue();
        value++;
        a.setValue(value);
        return value;
    }

}