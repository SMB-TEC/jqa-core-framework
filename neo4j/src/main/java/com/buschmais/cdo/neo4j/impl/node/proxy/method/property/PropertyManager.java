package com.buschmais.cdo.neo4j.impl.node.proxy.method.property;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.neo4j.impl.node.metadata.RelationshipMetadata;
import com.buschmais.cdo.neo4j.spi.DatastoreSession;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.Iterator;

/**
 * Contains methods for reading and creating relationships specified by the given metadata.
 * <p/>
 * <p>For each provided method the direction of the relationships is handled transparently for the caller.</p>
 */
public class PropertyManager<EntityId, Entity, RelationId, Relation> {

    private DatastoreSession<EntityId, Entity, RelationId, Relation> datastoreSession;

    /**
     * Constructor.
     *
     * @param datastoreSession
     */
    public PropertyManager(DatastoreSession<EntityId, Entity, RelationId, Relation> datastoreSession) {
        this.datastoreSession = datastoreSession;
    }

    /**
     * Get the target node of a single relationship.
     *
     * @param source The source entity.
     * @return The target node or <code>null</code>.
     */
    public Entity getSingleRelation(Entity source, RelationshipMetadata metadata, RelationshipMetadata.Direction direction) {
        Relation relation = datastoreSession.getSingleRelation(source, metadata, direction);
        return relation != null ? getRelativeTarget(relation, direction) : null;
    }

    /**
     * Return all relationships of a node.
     *
     * @param source The node.
     * @return An iterator delivering all target nodes.
     */
    public Iterator<Entity> getRelations(Entity source, RelationshipMetadata metadata, final RelationshipMetadata.Direction direction) {
        Iterable<Relation> relations = datastoreSession.getRelations(source, metadata, direction);
        final Iterator<Relation> iterator = relations.iterator();
        return new Iterator<Entity>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entity next() {
                Relation next = iterator.next();
                return getRelativeTarget(next, direction);
            }

            @Override
            public void remove() {
            }
        };
    }

    /**
     * Create a single relationship from a source to another.
     * <p>An existing relationship will be discarded.</p>
     *
     * @param source The source.
     * @param target The target source or <code>null</code>.
     */
    public void createSingleRelation(Entity source, RelationshipMetadata metadata, RelationshipMetadata.Direction direction, Entity target) {
        if (datastoreSession.hasRelation(source, metadata, direction)) {
            Relation relation = datastoreSession.getSingleRelation(source, metadata, direction);
            datastoreSession.deleteRelation(relation);
        }
        if (target != null) {
            datastoreSession.createRelation(source, metadata, direction, target);
        }
    }

    /**
     * Create a relationship to a source.
     *
     * @param source The source.
     * @param target The target source.
     */
    public void createRelation(Entity source, RelationshipMetadata metadata, RelationshipMetadata.Direction direction, Entity target) {
        datastoreSession.createRelation(source, metadata, direction, target);
    }

    /**
     * Remove an existing relationship.
     *
     * @param source The node.
     * @param target The target node.
     * @return <code>true</code> if an existing relationship has been removed.
     */
    public boolean removeRelation(Entity source, RelationshipMetadata metadata, RelationshipMetadata.Direction direction, Entity target) {
        Iterable<Relation> relations = datastoreSession.getRelations(source, metadata, direction);
        for (Relation relation : relations) {
            if (getRelativeTarget(relation, direction).equals(target)) {
                datastoreSession.deleteRelation(relation);
                return true;
            }
        }
        return false;
    }

    /**
     * Remove all relationships of a entity.
     *
     * @param source The entity.
     */
    public void removeRelations(Entity source, RelationshipMetadata metadata, RelationshipMetadata.Direction direction) {
        Iterable<Relation> relations = datastoreSession.getRelations(source, metadata, direction);
        for (Relation relation : relations) {
            datastoreSession.deleteRelation(relation);
        }
    }

    private Entity getRelativeTarget(Relation relation, RelationshipMetadata.Direction direction) {
        switch (direction) {
            case OUTGOING:
                return datastoreSession.getTarget(relation);
            case INCOMING:
                return datastoreSession.getSource(relation);
            default:
                throw new CdoException("Unsupported direction: " + direction);
        }
    }
}
