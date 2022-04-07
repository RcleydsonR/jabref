package org.jabref.logic.importer.fetcher;

import java.net.URL;
import java.util.List;

import org.jabref.logic.importer.FetcherException;
import org.jabref.logic.importer.fetcher.transformers.AbstractQueryTransformer;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.StandardEntryType;
import org.jabref.testutils.category.FetcherTest;

import org.apache.lucene.queryparser.flexible.core.nodes.QueryNode;
import org.apache.lucene.queryparser.flexible.standard.parser.StandardSyntaxParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FetcherTest
public class BHLFetcherTest {

    private BHLFetcher fetcher;

    @BeforeEach
    public void setUp() {
        fetcher = new BHLFetcher();
    }

    @Test
    public void testGetName() {
        assertEquals("BHL", fetcher.getName());
    }

    @Test
    public void simpleSearchQueryURLCorrect() throws Exception {
        String expectedCorrectURL = "https://www.biodiversitylibrary.org/api3?op=PublicationSearch&searchTerm=Arara&pageSize=50&apikey=7f78786d-e6df-427c-9e44-da6d8583ceba&format=json";
        String query = "Arara";
        QueryNode luceneQuery = new StandardSyntaxParser().parse(query, AbstractQueryTransformer.NO_EXPLICIT_FIELD);
        URL url = fetcher.getURLForQuery(luceneQuery);
        assertEquals(expectedCorrectURL, url.toString());
    }

    @Test
    public void testSearchBySpecificQueryFindExpectedBibEntry() throws FetcherException {
        BibEntry expected = new BibEntry(StandardEntryType.Book);
        expected.setField(StandardField.AUTHOR, "Göldi, Emil August,");
        expected.setField(StandardField.PUBLISHER, "Impressão do Instituto Polygraphico a.g.,");
        expected.setField(StandardField.TITLE, "Album de aves amazonicas");
        expected.setField(StandardField.LOCATION, "Zürich");
        expected.setField(StandardField.URL, "https://www.biodiversitylibrary.org/item/49021");
        expected.setField(StandardField.YEAR, "1900");

        List<BibEntry> resultEntries = fetcher.performSearch("Göldi, Emil August");
        assertTrue(resultEntries.stream().anyMatch(e -> e.getFields().equals(expected.getFields())));
    }

    @Test
    void testSearchByEmptyQueryDoNotFindAnyBibEntry() throws FetcherException {
        List<BibEntry> resultEntries = fetcher.performSearch("");
        assertEquals(emptyList(), resultEntries);
    }
}
