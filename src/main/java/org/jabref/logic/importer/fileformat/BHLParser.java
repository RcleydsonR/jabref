package org.jabref.logic.importer.fileformat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jabref.logic.importer.ParseException;
import org.jabref.logic.importer.Parser;
import org.jabref.logic.util.OS;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.EntryType;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class BHLParser implements Parser {

    @Override
    public List<BibEntry> parseEntries(InputStream inputStream) throws ParseException {
        String response = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(OS.NEWLINE));
        JSONObject jsonObject = new JSONObject(response);

        JSONArray results = jsonObject.getJSONArray("Result");

        return this.parseEntries(results);
    }

    public List<BibEntry> parseEntries(JSONArray response) throws ParseException {
        List<BibEntry> entries = new ArrayList<>();

        for (Object jsonSingleObject : response) {
            BibEntry entry = new BibEntry();
            JSONObject jsonSingleData = (JSONObject) jsonSingleObject;

            if (jsonSingleData.has("Authors")) {
                JSONArray authorJson = jsonSingleData.getJSONArray("Authors");
                List<String> authorList = IntStream.range(0, authorJson.length()).mapToObj(i -> authorJson.getJSONObject(i).getString("Name")).toList();
                entry.setField(StandardField.AUTHOR, String.join(" and ", authorList));
            }

            if (jsonSingleData.has("Genre")) {
                entry.setType(new EntryType() {
                    @Override
                    public String getName() {
                        return jsonSingleData.getString("Genre").toLowerCase();
                    }

                    @Override
                    public String getDisplayName() {
                        return jsonSingleData.getString("Genre").toLowerCase();
                    }
                });
            }

            if (jsonSingleData.has("Title")) {
                entry.setField(StandardField.TITLE, jsonSingleData.getString("Title"));
            }

            if (jsonSingleData.has("Date")) {
                entry.setField(StandardField.YEAR, jsonSingleData.getString("Date"));
            }

            if (jsonSingleData.has("PublicationDate")) {
                entry.setField(StandardField.YEAR, jsonSingleData.getString("PublicationDate"));
            }

            if (jsonSingleData.has("Volume")) {
                entry.setField(StandardField.VOLUME, jsonSingleData.getString("Volume"));
            }

            if (jsonSingleData.has("PublisherPlace")) {
                entry.setField(StandardField.LOCATION, jsonSingleData.getString("PublisherPlace"));
            }

            if (jsonSingleData.has("PublisherName")) {
                entry.setField(StandardField.PUBLISHER, jsonSingleData.getString("PublisherName"));
            }

            if (jsonSingleData.has("ItemUrl")) {
                entry.setField(StandardField.URL, jsonSingleData.getString("ItemUrl"));
            }

            entries.add(entry);
        }

        return entries;
    }
}
