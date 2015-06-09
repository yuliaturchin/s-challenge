package com.challenge.search.impl;

import com.challenge.search.TermStatistics;

import java.util.*;

/**
 * Created by YULIAT on 06/07/2015.
 */
class DocumentIndex {

    private final String indexName;
    private Map<String, List<String>> index2EntryIDs = new HashMap<>();
    private Map<String, TermStatistics> value2statistics = new HashMap<>();
    private final String termsSeparator;

    private int termsCounter = 0;

    DocumentIndex(String indexName, String termsSeparator)
    {
        this.indexName = indexName;
        this.termsSeparator = termsSeparator;
    }


    String getIndexName ()
    {
        return indexName;
    }


    void indexDocument(String documentID, String document) {

        StringTokenizer tokenizer = new StringTokenizer(document, termsSeparator);
        while (tokenizer.hasMoreTokens())
        {
            String term = tokenizer.nextToken();
            if (index2EntryIDs.get(term)==null)
            {
                index2EntryIDs.put(term, new LinkedList<String>());
                termsCounter++;
            }
            List<String> listings = index2EntryIDs.get(term);
            listings.add(documentID);
            updateTermStatistics(term);
        }
    }


    public int getTermsCounter ()
    {
        return termsCounter;
    }


    List<String> searchDocuments(String term) {
        List<String> entityIDs = index2EntryIDs.get(term);
        return entityIDs;
    }

    TermStatistics getTermStatistics(String term)
    {
        if (value2statistics.get(term)==null)
        {
            return null;
        }
        return value2statistics.get(term);
    }


    private void updateTermStatistics(String term) {

        if (value2statistics.get(term)==null)
        {
            value2statistics.put(term, new TermStatistics(term,1));
        }
        else {
            TermStatistics stats = this.value2statistics.get(term);
            stats.setFrequency(stats.getFrequency() + 1);
        }
    }

}
