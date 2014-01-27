package com.buschmais.cdo.store.json.api;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.buschmais.cdo.spi.bootstrap.CdoDatastoreProvider;
import com.buschmais.cdo.spi.datastore.Datastore;
import com.buschmais.cdo.store.json.impl.JsonFileStore;
import com.buschmais.cdo.store.json.impl.JsonFileStoreSession;
import com.buschmais.cdo.store.json.impl.metadata.JsonNodeMetadata;

import java.net.MalformedURLException;
import java.net.URI;

public class JsonFileStoreProvider implements CdoDatastoreProvider {

    @Override
    public Datastore<JsonFileStoreSession, JsonNodeMetadata, String> createDatastore(CdoUnit cdoUnit) {
        URI uri = cdoUnit.getUri();
        if (!"file".equals(uri.getScheme())) {
            throw new CdoException("Only file URIs are supported by this store.");
        }
        try {
            return new JsonFileStore(uri.toURL().getPath());
        } catch (MalformedURLException e) {
            throw new CdoException("Cannot convert URI '" + uri.toString() + "' to URL.", e);
        }
    }
}
