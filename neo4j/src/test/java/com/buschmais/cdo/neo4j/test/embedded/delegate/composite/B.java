package com.buschmais.cdo.neo4j.test.embedded.delegate.composite;

import com.buschmais.cdo.neo4j.api.annotation.Label;

@Label("B")
public interface B {

    A2B getA2B();

}