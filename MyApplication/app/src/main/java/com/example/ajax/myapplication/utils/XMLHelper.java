package com.example.ajax.myapplication.utils;

import com.example.ajax.myapplication.model.Author;
import com.example.ajax.myapplication.model.Book;
import com.example.ajax.myapplication.model.viewmodel.AuthorModel;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.settings.ISettings;
import com.example.ajax.myapplication.settings.impl.Settings;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

public final class XMLHelper {

    private static final Object lock = new Object();
    private static ISettings mSettings = new Settings();

    public static void updateSettings() {
        synchronized (lock) {
            mSettings = new Settings();
        }
    }

    public static AuthorModel parseAuthor(final String response) {
        final Document doc;
        final AuthorModel authorModel = new AuthorModel();
        final List<BookModel> bookModels = new ArrayList<>();
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(response));
            doc = documentBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();
            final NodeList author = doc.getElementsByTagName(Constants.AUTHOR_TAG);
            final Element authorElement = (Element) author.item(0);
            authorModel.setId(Long.parseLong(authorElement.getElementsByTagName(Author.ID).item(0).getNodeValue()));
            final String imageUrl;
            if (mSettings.downloadLarge()) {
                imageUrl = authorElement.getElementsByTagName(Constants.LARGE_IMAGE_URL).item(0).getNodeName();
            } else {
                imageUrl = authorElement.getElementsByTagName(Book.IMAGE_URL).item(0).getNodeValue();
            }
            authorModel.setImage(imageUrl);
            authorModel.setDescription(authorElement.getElementsByTagName(Author.ABOUT).item(0).getNodeValue());
            authorModel.setName(authorElement.getElementsByTagName(Author.NAME).item(0).getNodeValue());
            final NodeList books = doc.getElementsByTagName(Constants.BOOKS_TAG);
            for (int i = 0; i < books.getLength(); i++) {

            }

        } catch (SAXException | ParserConfigurationException | IOException pE) {
            pE.printStackTrace();
        }
        return authorModel;
    }

    public static List<BookModel> parseSearch(final String response) {
        final List<BookModel> books = new ArrayList<>();
        final Document doc;
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(response));
            doc = documentBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();
            final NodeList workNodes = doc.getElementsByTagName(Constants.WORK_TAG);
            for (int i = 0; i < workNodes.getLength(); i++) {
                final BookModel book = new BookModel();
                final AuthorModel author = new AuthorModel();
                final Element work = (Element) workNodes.item(i);
                final NodeList bestBook = work.getElementsByTagName(Constants.BOOK_TAG);
                for (int j = 0; j < bestBook.getLength(); j++) {
                    final Element bestBookNode = (Element) bestBook.item(j);
                    book.setRating(Float.parseFloat(work.getElementsByTagName(Book.RATING).item(j).getChildNodes()
                            .item(0).getNodeValue()));
                    book.setTitle(bestBookNode.getElementsByTagName(Book.TITLE).item(0).getChildNodes().item(0)
                            .getNodeValue());
                    book.setId(Long.parseLong(bestBookNode.getElementsByTagName(Book.ID).item(0).getChildNodes().item(0)
                            .getNodeValue()));
                    final Element authorNode = (Element) bestBookNode.getElementsByTagName(Constants.AUTHOR_TAG).item(0);
                    author.setName(authorNode.getElementsByTagName(Author.NAME).item(0).getFirstChild().getNodeValue());
                    author.setId(Long.parseLong(authorNode.getElementsByTagName(Author.ID).item(0).getFirstChild()
                            .getNodeValue()));
                    book.setImage(bestBookNode.getElementsByTagName(Book.IMAGE_URL).item(0).getChildNodes().item(0)
                            .getNodeValue());
                    book.setAuthorId(author.getId());
                    book.setAuthorName(author.getName());

                }

                books.add(book);
            }
            /*new Thread(new Runnable() {

                @Override
                public void run() {
                    final DBHelper helper = new DBHelper(ContextHolder.get(), null, Constants.DATABASE_VERSION);
                    final List<ContentValues> values = new ArrayList<>();
                    for (final BookModel book : books) {
                        final ContentValues contentValues = new ContentValues();
                        contentValues.put(Book.ID, book.getId());
                        contentValues.put(Book.TITLE, book.getTitle());
                        contentValues.put(Book.IMAGE_URL, book.getImageUrl());
                        contentValues.put(Book.RATING, book.getRating());
                        contentValues.put(Book.AUTHOR_ID, book.getAuthorId());
                        values.add(contentValues);
                    }

                    //helper.bulkInsert(Book.class, values);
                    final List<ContentValues> contentValues = new ArrayList<>();
                    for (final AuthorModel author : authors) {
                        final ContentValues contentValue = new ContentValues();
                        contentValue.put(Author.ID, author.getId());
                        contentValue.put(Author.NAME, author.getName());
                        contentValues.add(contentValue);
                    }
                    //helper.bulkInsert(Author.class, contentValues);
                }
            }).start();*/

        } catch (ParserConfigurationException | SAXException | IOException pE) {
            pE.printStackTrace();
        }
        return books;

    }

    public static List<BookModel> parseSimilar(final String response) {
        final Document doc;
        final List<BookModel> similarBooks = new ArrayList<>();
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(response));
            doc = documentBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();

            final NodeList similarBooksTag = doc.getElementsByTagName(Constants.SIMILAR_BOOK_TAG);

            final BookModel bookToSkip = new BookModel();
            bookToSkip.setDescription(doc.getElementsByTagName(Book.DESCRIPTION).item(0).getChildNodes().item(0).getNodeValue());
            if (mSettings.downloadLarge()) {
                bookToSkip.setImage(doc.getElementsByTagName(Constants.LARGE_IMAGE_URL).item(0).getChildNodes().item(0)
                        .getNodeValue());
            }
            similarBooks.add(bookToSkip);

            if (similarBooksTag == null) {
                return similarBooks;
            }

            final NodeList similarBooksArray = similarBooksTag.item(0).getChildNodes();
            for (int i = 0; i < similarBooksArray.getLength(); i++) {
                final BookModel book = new BookModel();
                final AuthorModel author = new AuthorModel();
                if (!(similarBooksArray.item(i) instanceof Element)) {
                    continue;
                }
                final Element similarBook = (Element) similarBooksArray.item(i);

                book.setId(Long.parseLong(similarBook.getElementsByTagName(Book.ID).item(0).getChildNodes().item(0)
                        .getNodeValue()));
                book.setImage(similarBook.getElementsByTagName(Book.IMAGE_URL).item(0).getChildNodes().item(0)
                        .getNodeValue());
                book.setTitle(similarBook.getElementsByTagName(Book.TITLE).item(0).getChildNodes().item(0)
                        .getNodeValue());
                book.setRating(Float.parseFloat(similarBook.getElementsByTagName(Book.RATING).item(0).getChildNodes()
                        .item(0).getNodeValue()));

                final Element authorNode = (Element) similarBook.getElementsByTagName(Constants.AUTHOR_TAG).item(0);
                author.setName(authorNode.getElementsByTagName(Author.NAME).item(0).getFirstChild().getNodeValue());
                author.setId(Long.parseLong(authorNode.getElementsByTagName(Author.ID).item(0).getFirstChild()
                        .getNodeValue()));
                book.setAuthorId(author.getId());
                book.setAuthorName(author.getName());
                similarBooks.add(book);
            }
            return similarBooks;
        } catch (SAXException | ParserConfigurationException | IOException pE) {
            pE.printStackTrace();
        }
        return null;
    }

}
