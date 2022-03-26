package org.jabref.model.entry;

import java.util.Collections;
import java.util.List;

import org.jabref.model.entry.field.BibField;
import org.jabref.model.entry.field.FieldPriority;
import org.jabref.model.entry.field.OrFields;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.EntryType;
import org.jabref.model.entry.types.EntryTypeFactory;
import org.jabref.model.entry.types.IEEETranEntryType;
import org.jabref.model.entry.types.StandardEntryType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EntryTypeFactoryTest {
    private BibEntryType simpleArticle;
    private BibEntryType simpleBook;

    @BeforeEach
    void setUp() {
        simpleArticle = new BibEntryType(
                StandardEntryType.Article,
                List.of(
                        new BibField(StandardField.MONTH, FieldPriority.IMPORTANT),
                        new BibField(StandardField.NUMBER, FieldPriority.IMPORTANT),
                        new BibField(StandardField.PAGES, FieldPriority.IMPORTANT),
                        new BibField(StandardField.VOLUME, FieldPriority.IMPORTANT)
                ),
                Collections.singleton(
                        new OrFields(StandardField.AUTHOR, StandardField.JOURNAL, StandardField.TITLE, StandardField.YEAR)
                ));
        simpleBook = new BibEntryType(
                StandardEntryType.Book,
                List.of(
                        new BibField(StandardField.MONTH, FieldPriority.IMPORTANT),
                        new BibField(StandardField.NUMBER, FieldPriority.IMPORTANT),
                        new BibField(StandardField.PAGES, FieldPriority.IMPORTANT),
                        new BibField(StandardField.VOLUME, FieldPriority.IMPORTANT)
                ),
                Collections.singleton(
                        new OrFields(StandardField.AUTHOR, StandardField.JOURNAL, StandardField.TITLE, StandardField.YEAR)
                        ));
    }

    @Test
    public void testParseEntryTypePatent() {
        EntryType patent = IEEETranEntryType.Patent;
        assertEquals(patent, EntryTypeFactory.parse("patent"));
    }

    @Test
    public void testAreEqualNullBibEntryTypes() {
        assertTrue(EntryTypeFactory.isEqualNameAndFieldBased(null, null));
    }

    @Test
    public void testAreNotEqualNullAndNotNullBibEntryTypes() {
        assertFalse(EntryTypeFactory.isEqualNameAndFieldBased(null, simpleArticle));
        assertFalse(EntryTypeFactory.isEqualNameAndFieldBased(simpleArticle, null));
    }

    @Test
    public void testAreNotEqualNotNullBibEntryTypes() {
        // Different Types
        assertFalse(EntryTypeFactory.isEqualNameAndFieldBased(simpleArticle, simpleBook));

        // Different Required Fields
        BibEntryType articleWithMissingRequiredFields = new BibEntryType(
                StandardEntryType.Article,
                List.of(
                        new BibField(StandardField.MONTH, FieldPriority.IMPORTANT),
                        new BibField(StandardField.NUMBER, FieldPriority.IMPORTANT),
                        new BibField(StandardField.PAGES, FieldPriority.IMPORTANT),
                        new BibField(StandardField.VOLUME, FieldPriority.IMPORTANT)
                ),
                Collections.singleton(
                        new OrFields(StandardField.AUTHOR)
                ));
        assertFalse(EntryTypeFactory.isEqualNameAndFieldBased(simpleArticle, articleWithMissingRequiredFields));

        // Different Optional Fields
        BibEntryType articleWithMissingOptionalFields = new BibEntryType(
                StandardEntryType.Article,
                List.of(
                        new BibField(StandardField.MONTH, FieldPriority.IMPORTANT)
                ),
                Collections.singleton(
                        new OrFields(StandardField.AUTHOR, StandardField.JOURNAL, StandardField.TITLE, StandardField.YEAR)
                ));
        assertFalse(EntryTypeFactory.isEqualNameAndFieldBased(simpleArticle, articleWithMissingOptionalFields));

        // Different Secondary Optional Fields
        // This test will always fail because the getOptionalFields always returns the primary and secondary optional
        // fields, so this condition is useless.
        BibEntryType articleWithSecondaryOptionalFields = new BibEntryType(
                StandardEntryType.Article,
                List.of(
                        new BibField(StandardField.MONTH, FieldPriority.IMPORTANT),
                        new BibField(StandardField.NUMBER, FieldPriority.IMPORTANT),
                        new BibField(StandardField.PAGES, FieldPriority.IMPORTANT),
                        new BibField(StandardField.VOLUME, FieldPriority.IMPORTANT),
                        new BibField(StandardField.PUBLISHER, FieldPriority.DETAIL)
                ),
                Collections.singleton(
                        new OrFields(StandardField.AUTHOR, StandardField.JOURNAL, StandardField.TITLE, StandardField.YEAR)
                ));
        assertFalse(EntryTypeFactory.isEqualNameAndFieldBased(simpleArticle, articleWithSecondaryOptionalFields));
    }

    @Test
    public void testAreEqualNotNullBibEntryTypes() {
        assertTrue(EntryTypeFactory.isEqualNameAndFieldBased(simpleArticle, simpleArticle));
    }
}
