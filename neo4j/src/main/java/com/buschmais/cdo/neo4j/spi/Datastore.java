package com.buschmais.cdo.neo4j.spi;

import com.buschmais.cdo.neo4j.impl.common.reflection.BeanMethod;
import com.buschmais.cdo.neo4j.impl.common.reflection.PropertyMethod;
import com.buschmais.cdo.neo4j.impl.node.metadata.MetadataProvider;
import com.buschmais.cdo.neo4j.impl.node.metadata.RelationMetadata;

public interface Datastore<DS extends DatastoreSession> {

    DS createSession(MetadataProvider metadataProvider);

    void close();

    // Metadata

    <EntityMetadata> EntityMetadata createEntityMetadata(Class<?> type);

    <ImplementedByMetadata> ImplementedByMetadata createImplementedByMetadata(BeanMethod beanMethod);

    <CollectionPropertyMetadata> CollectionPropertyMetadata createCollectionPropertyMetadata(PropertyMethod beanPropertyMethod);

    <ReferencePropertyMetadata> ReferencePropertyMetadata createReferencePropertyMetadata(PropertyMethod beanPropertyMethod);

    <PrimitivePropertyMetadata> PrimitivePropertyMetadata createPrimitvePropertyMetadata(PropertyMethod beanPropertyMethod);

    <EnumPropertyMetadata> EnumPropertyMetadata createEnumPropertyMetadata(PropertyMethod beanPropertyMethod);

    <ImplementedByMetadata> ImplementedByMetadata createIndexedPropertyMetadata(PropertyMethod beanMethod);

    <RelationMetadata> RelationMetadata createRelationMetadata(PropertyMethod beanPropertyMethod);

    RelationMetadata.Direction getRelationDirection(PropertyMethod beanPropertyMethod);
}
