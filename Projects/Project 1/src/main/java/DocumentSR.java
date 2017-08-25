import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;

import javax.swing.*;
import java.util.List;


/**
 * Created by Albin on 2016-09-22.
 */
public class DocumentSR {
    Document document;
    int documentCount;
    int minOccurrence;
    int maxOccurrence;

    public DocumentSR(Attributes attr) {
        this.document = attr.document;
        this.minOccurrence = attr.occurrence;
        this.maxOccurrence = attr.occurrence;
        this.documentCount = 1;
    }

    public void mergeDocumentSR(Attributes attr) {
        if (!this.document.name.equals(attr.document.name)) return;

        if (this.maxOccurrence < attr.occurrence)
            this.maxOccurrence = attr.occurrence;
        else if (this.minOccurrence > attr.occurrence)
            this.minOccurrence = attr.occurrence;

        documentCount++;
    }

    public String getDocName() {
        return this.document.name;
    }

    public int compareTo(String item) {
        return this.document.name.compareTo(item);
    }

    public int compareTo(DocumentSR b, SortOrder order, boolean ascending) {
        int result = 0;
        switch (order) {
            case NAME:
                result = this.compareTo(b.getDocName());
                break;
            case COUNT:
                result = this.documentCount - b.documentCount;
                break;
            case POPULARITY:
                result = this.document.popularity - b.document.popularity;
                break;
            case OCCURRENCE:
                result = this.maxOccurrence - b.maxOccurrence;
                break;
        }

        if (!ascending) {
            result *= -1;
        }

        return result;
    }
}
