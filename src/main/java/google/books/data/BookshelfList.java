package google.books.data;


import lombok.Data;

import java.util.List;

@Data
class AccessInfo {
    private String country;
    private String viewability;
    private Boolean embeddable;
    private Boolean publicDomain;
    private String textToSpeechPermission;
    private Epub epub;
    private Pdf pdf;
    private String webReaderLink;
    private String accessViewStatus;
    private Boolean quoteSharingAllowed;
}

@Data
class Epub {
    private Boolean isAvailable;
    private String acsTokenLink;
}


@Data
class ImageLinks {
    private String smallThumbnail;
    private String thumbnail;
    private String small;
    private String medium;
    private String large;
    private String extraLarge;
}

@Data
class Layer {
    private String layerId;
    private String volumeAnnotationsVersion;
}

@Data
class LayerInfo {
    private List<Layer> layers;
}

@Data
class Price {
    private Double amount;
    private String currencyCode;
}


@Data
class Availability {
    private Boolean isAvailable;
    private String acsTokenLink;
}
@Data
class Pdf {
    private Boolean isAvailable;
}


@Data
class ListPrice {
    private Double amount;
    private String currencyCode;
}

@Data
class RetailPrice {
    private Double amount;
    private String currencyCode;
}

@Data
class IndustryIdentifier {
    private String type;
    private String identifier;
}

@Data
class PanelizationSummary {
    private Boolean containsEpubBubbles;
    private Boolean containsImageBubbles;
}


@Data
class ReadingModes {
    private Boolean text;
    private Boolean image;
}


@Data
class Offer {
    private Integer finskyOfferType;
    private RetailPrice listPrice;
    private RetailPrice retailPrice;
}

@Data
class SaleInfo {
    private String country;
    private String saleability;
    private Boolean isEbook;
    private ListPrice listPrice;

    private Price price;
    private RetailPrice retailPrice;
    private String buyLink;
    private List<Offer> offers;
}


@Data
class SearchInfo {
    private String textSnippet;
}

@Data
class VolumeInfo {
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private List<IndustryIdentifier> industryIdentifiers;
    private ReadingModes readingModes;
    private Integer pageCount;
    private String printType;
    private List<String> categories;
    private Double averageRating;
    private Integer ratingsCount;
    private String maturityRating;
    private Boolean allowAnonLogging;
    private String contentVersion;
    private PanelizationSummary panelizationSummary;
    private ImageLinks imageLinks;
    private String language;
    private String previewLink;
    private String infoLink;
    private String canonicalVolumeLink;
}

@Data
class Bookshelf {
    private String kind;
    private int id;
    private String title;
    private String access;
    private String updated;
    private String created;
    private int volumeCount;
    private String volumesLastUpdated;
}

@Data
public class BookshelfList {
    private String kind;
    private List<Bookshelf> items;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Kind: ").append(kind).append("\n");
        sb.append("Items: \n");
        items.forEach(item -> sb.append(item.toString()).append("\n"));
        return sb.toString();
    }
}


