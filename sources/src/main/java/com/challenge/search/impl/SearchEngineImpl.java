package com.challenge.search.impl;

import com.challenge.search.SearchEngine;
import com.challenge.search.TermStatistics;
import com.challenge.utils.CommonMethods;

import java.util.*;

/**
 * Created by YULIAT on 06/07/2015.
 */
public class SearchEngineImpl<T> implements SearchEngine<T> {

    private Map<String, T> entryID2entry = new HashMap<>();
    private Map<String, DocumentIndex> indexName2index = new HashMap<>();
    private SearchConfiguration configuration;


    public SearchEngineImpl (SearchConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public String getTermStatisticsSummary() {
        StringBuilder builder = new StringBuilder();
        for (DocumentIndex index: indexName2index.values())
        {
            builder.append(String.format("[%s: %s terms] ", index.getIndexName(), index.getTermsCounter()));
        }
        return builder.toString();
    }

    public String register(T entry, Map<String, String> propertiesToIndex) {
        String id = CommonMethods.nextUUID();
        entryID2entry.put(id, entry);

        updatePropertiesIndexes(id, propertiesToIndex);
        return id;
    }


    public List<T> search(String property, Operator operator, String value) {
        DocumentIndex indexToSearch = indexName2index.get(property);
        if (indexToSearch == null) {
            return null;
        }

        List<String> entityIDs = indexToSearch.searchDocuments(value);

        List<T> entities = new LinkedList<>();
        for (String entityID : entityIDs) {
            entities.add(entryID2entry.get(entityID));
        }
        return entities;
    }

    @Override
    public TermStatistics getTermStatisticsByProperty(String property, String term) {
        return indexName2index.get(property).getTermStatistics(term);
    }


    private void updatePropertiesIndexes(String objectID, Map<String, String> propery2value) {
        for (String indexName : propery2value.keySet()) {
            updatePropertyIndex(objectID, indexName, propery2value.get(indexName));
        }
    }

    private void updatePropertyIndex(String objectID, String propertyName, String propertyValue) {

        if (indexName2index.get(propertyName) == null) {
            indexName2index.put(propertyName, new DocumentIndex(propertyName, configuration.getTermsSeparator()));
        }
        DocumentIndex index = indexName2index.get(propertyName);
        index.indexDocument(objectID, propertyValue);
    }
}
