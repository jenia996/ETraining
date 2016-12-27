package com.example.ajax.myapplication.utils;

import com.example.ajax.myapplication.model.Author;
import com.example.ajax.myapplication.model.Book;
import com.example.ajax.myapplication.model.viewmodel.AuthorModel;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.settings.ISettings;
import com.example.ajax.myapplication.settings.impl.Settings;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public final class XMLParser {

    private static final Object lock = new Object();
    private static ISettings mSettings = new Settings();

    public static void updateSettings() {
        synchronized (lock) {
            mSettings = new Settings();
        }
    }

    public static AuthorModel parseAuthorBooks(final String response) throws IOException, SAXException,
            ParserConfigurationException {
        final Document doc;
        final AuthorModel authorModel = new AuthorModel();
        final List<BookModel> bookModels = new ArrayList<>();
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(response));
        doc = documentBuilder.parse(inputSource);
        doc.getDocumentElement().normalize();
        final NodeList books = doc.getElementsByTagName(Constants.BOOKS_TAG).item(0).getChildNodes();
        final Node about = doc.getElementsByTagName(Author.ABOUT).item(0).getFirstChild();

        if (about != null) {
            authorModel.setAbout(about.getNodeValue());
        }

        authorModel.setImage(doc.getElementsByTagName(Book.IMAGE_URL).item(0).getFirstChild().getNodeValue());

        for (int i = 0; i < books.getLength(); i++) {
            final BookModel bookModel = new BookModel();
            final AuthorModel author = new AuthorModel();
            if (!(books.item(i) instanceof Element)) {
                continue;
            }
            final Element bookElement = (Element) books.item(i);

            bookModel.setId(Long.parseLong(getValueByTag(bookElement, Book.ID)));
            bookModel.setImageUrl(getValueByTag(bookElement, Book.IMAGE_URL));
            bookModel.setTitle(getValueByTag(bookElement, Book.TITLE));
            bookModel.setRating(Float.parseFloat(getValueByTag(bookElement, Book.RATING)));

            final Element authorElement = (Element) bookElement.getElementsByTagName(Constants.AUTHOR_TAG).item(0);
            author.setName(getValueByTag(authorElement, Author.NAME));
            author.setId(Long.parseLong(getValueByTag(authorElement, Constants.AUTHOR_ID_TAG)));
            bookModel.setAuthorId(author.getId());
            bookModel.setAuthorName(author.getName());
            bookModels.add(bookModel);

        }
        authorModel.setBooks(bookModels);

        return authorModel;
    }

    public static List<BookModel> parseSearch(final String response) throws ParserConfigurationException,
            IOException, SAXException {
        final List<BookModel> books = new ArrayList<>();
        final Document doc;
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(response));
        doc = documentBuilder.parse(inputSource);
        doc.getDocumentElement().normalize();
        final NodeList workNodes = doc.getElementsByTagName(Constants.WORK_TAG);

        for (int i = 0; i < workNodes.getLength(); i++) {
            final BookModel bookModel = new BookModel();
            final AuthorModel author = new AuthorModel();
            final Element work = (Element) workNodes.item(i);
            final NodeList bookElements = work.getElementsByTagName(Constants.BOOK_TAG);
            for (int j = 0; j < bookElements.getLength(); j++) {
                final Element bookElement = (Element) bookElements.item(j);
                bookModel.setRating(Float.parseFloat(work.getElementsByTagName(Book.RATING).item(0).getFirstChild()
                        .getNodeValue()));
                bookModel.setTitle(bookElement.getElementsByTagName(Book.TITLE).item(0).getFirstChild().getNodeValue());
                bookModel.setId(Long.parseLong(bookElement.getElementsByTagName(Constants.AUTHOR_ID_TAG).item(0)
                        .getFirstChild()
                        .getNodeValue()));
                final Element authorNode = (Element) bookElement.getElementsByTagName(Constants.AUTHOR_TAG).item(0);
                author.setName(authorNode.getElementsByTagName(Author.NAME).item(0).getFirstChild().getNodeValue());
                author.setId(Long.parseLong(authorNode.getElementsByTagName(Constants.AUTHOR_ID_TAG).item(0)
                        .getFirstChild()
                        .getNodeValue()));
                bookModel.setImageUrl(bookElement.getElementsByTagName(Book.IMAGE_URL).item(0).getChildNodes().item(0)
                        .getNodeValue());
                bookModel.setAuthorId(author.getId());
                bookModel.setAuthorName(author.getName());

            }

            books.add(bookModel);
        }

        return books;
    }

    private static String getValueByTag(final Element pElement, final String pTag) {
        return pElement.getElementsByTagName(pTag).item(0).getFirstChild().getNodeValue();
    }

    public static List<BookModel> parseSimilar(final String response) throws IOException, SAXException,
            ParserConfigurationException {
        final Document doc;
        final List<BookModel> similarBooks = new ArrayList<>();
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(response));
        doc = documentBuilder.parse(inputSource);
        doc.getDocumentElement().normalize();

        final NodeList similarBooksTag = doc.getElementsByTagName(Constants.SIMILAR_BOOK_TAG);

        final BookModel bookToSkip = new BookModel();
        final Node description = doc.getElementsByTagName(Book.DESCRIPTION).item(0).
                getFirstChild();
        if (description != null) {
            bookToSkip.setDescription(description.getNodeValue());
        }
        if (mSettings.downloadLarge()) {
            bookToSkip.setImageUrl(doc.getElementsByTagName(Constants.LARGE_IMAGE_URL).item(0)
                    .getChildNodes().item(0)
                    .getNodeValue());
        }
        similarBooks.add(bookToSkip);

        if (similarBooksTag == null || similarBooksTag.item(0) == null) {
            return similarBooks;
        }

        final NodeList similarBooksArray = similarBooksTag.item(0).getChildNodes();
        for (int i = 0; i < similarBooksArray.getLength(); i++) {
            final BookModel book = new BookModel();
            final AuthorModel author = new AuthorModel();
            if (!(similarBooksArray.item(i) instanceof Element)) {
                continue;
            }
            final Element relatedBookElement = (Element) similarBooksArray.item(i);
            book.setId(Long.parseLong(getValueByTag(relatedBookElement, Book.ID)));
            book.setImageUrl(getValueByTag(relatedBookElement, Book.IMAGE_URL));
            book.setTitle(getValueByTag(relatedBookElement, Book.TITLE));
            book.setRating(Float.parseFloat(getValueByTag(relatedBookElement, Book.RATING)));

            final Element authorNode = (Element) relatedBookElement.getElementsByTagName(Constants
                    .AUTHOR_TAG).item(0);
            author.setName(getValueByTag(authorNode, Author.NAME));
            author.setId(Long.parseLong(getValueByTag(authorNode, Constants.AUTHOR_ID_TAG)));
            book.setAuthorId(author.getId());
            book.setAuthorName(author.getName());
            similarBooks.add(book);
        }
        return similarBooks;

    }

}
