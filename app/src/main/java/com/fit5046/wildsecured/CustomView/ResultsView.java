package com.fit5046.wildsecured.CustomView;


import com.fit5046.wildsecured.TfLite.Classifier;

import java.util.List;

public interface ResultsView {
    public void setResults(final List<Classifier.Recognition> results);
}
