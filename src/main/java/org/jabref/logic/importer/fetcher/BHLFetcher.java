package org.jabref.logic.importer.fetcher;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.jabref.logic.importer.FetcherException;
import org.jabref.logic.importer.Parser;
import org.jabref.logic.importer.SearchBasedParserFetcher;
import org.jabref.logic.importer.fetcher.transformers.DefaultQueryTransformer;
import org.jabref.logic.importer.fileformat.BHLParser;

import org.apache.http.client.utils.URIBuilder;
import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;

public class BHLFetcher implements SearchBasedParserFetcher {

    private static final String URL_PATTERN = "https://www.biodiversitylibrary.org/api3?";
    private static final String BHL_API_KEY = "7f78786d-e6df-427c-9e44-da6d8583ceba";

    public BHLFetcher() { }

    @Override
    public String getName() {
        return "BHL";
    }

    @Override
    public URL getURLForQuery(QueryNode luceneQuery) throws URISyntaxException, MalformedURLException, FetcherException {
        URIBuilder uriBuilder = new URIBuilder(URL_PATTERN);
        uriBuilder.addParameter("op", "PublicationSearch");
        uriBuilder.addParameter("searchTerm", new DefaultQueryTransformer().transformLuceneQuery(luceneQuery).orElse(""));
        uriBuilder.addParameter("pageSize", "50");
        uriBuilder.addParameter("apikey", BHL_API_KEY);
        uriBuilder.addParameter("format", "json");
        return uriBuilder.build().toURL();
    }

    @Override
    public Parser getParser() {
        return new BHLParser();
    }
}
