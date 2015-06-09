package com.challenge.search;

import java.util.List;
import java.util.Map;

/**
 * Created by YULIAT on 06/07/2015.
 */
public interface SearchEngine<T> {

    String getTermStatisticsSummary();

    enum Operator {CONTAINS}

    /**
     * Registers the provided entity for the search
     * @param entity entity to register
     * @param searchIndexes the indexes according to which to store and search the entity
     * @return
     */
    public String register (T entity, Map<String,String> searchIndexes);

    /**
     * Performs search of products satisfying the provided criteria of the Product' terms.
     * Currently supports CONTAINS criteria.
     * @param indexName
     * @param operator
     * @param term
     * @return Products satisfying the provided term criteria.
     */
    public List<T> search(String indexName, Operator operator, String term);

    /**
     * Returns the term statistics in the index.
     * @param index
     * @param term
     * @return
     */
    public TermStatistics getTermStatisticsByProperty(String index, String term);
}
