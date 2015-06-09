package com.challenge.search;

/**
 * Created by YULIAT on 06/08/2015.
 */
public class TermStatistics implements Comparable<TermStatistics> {

    private final String term;
    private int frequency;

    public TermStatistics (String term, int frequency)
    {
        this.term=term;
        this.frequency = frequency;
    }

    public String getTerm() {
        return term;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(TermStatistics termToCompareTo) {
        return frequency>termToCompareTo.frequency?1
                :(frequency<termToCompareTo.frequency)?-1:0;
    }


}
