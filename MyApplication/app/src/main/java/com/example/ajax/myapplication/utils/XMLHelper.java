package com.example.ajax.myapplication.utils;

import com.example.ajax.myapplication.database.DBHelper;
import com.example.ajax.myapplication.model.entity.Author;
import com.example.ajax.myapplication.model.entity.Book;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public final class XMLHelper {

    private static final XMLHelper instance = new XMLHelper();
    private final DBHelper mDBHelper;

    private XMLHelper() {
        mDBHelper = new DBHelper(ContextHolder.get(), null, 1);
    }

    public static XMLHelper getInstance() {
        return instance;
    }

    List<Book> parseSimilar(final String response) {
        final Document doc;
        final List<Book> similarBooks = new ArrayList<>();
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(response));
            doc = documentBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();

            final NodeList similarBooksArray = doc.getElementsByTagName("similar_books");
            for (int i = 0; i < similarBooksArray.getLength(); i++) {
                final Book book = new Book();
                final Author author = new Author();
                final Element similarBook = (Element) similarBooksArray.item(i);
                book.setId(Long.parseLong(similarBook.getElementsByTagName("id").item(0).getChildNodes().item(0)
                        .getNodeValue()));
                book.setImage(similarBook.getElementsByTagName("image_url").item(0).getChildNodes().item(0)
                        .getNodeValue());
                book.setTitle(similarBook.getElementsByTagName("title").item(0).getChildNodes().item(0)
                        .getNodeValue());
                book.setIsbn(similarBook.getElementsByTagName("isbn13").item(0).getChildNodes().item(0)
                        .getNodeValue());
                book.setRating(Float.parseFloat(similarBook.getElementsByTagName("average_rating").item(0).getChildNodes()
                        .item(0).getNodeValue()));

                final Element authorNode = (Element) similarBook.getElementsByTagName("author").item(0);
                author.setName(authorNode.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
                author.setId(Long.parseLong(authorNode.getElementsByTagName("id").item(0).getFirstChild()
                        .getNodeValue()));
                book.setAuthorId(author);
                similarBooks.add(book);
            }
            return similarBooks;
        } catch (SAXException | ParserConfigurationException | IOException pE) {
            pE.printStackTrace();
        }
        return null;
    }

    Book parseBookInfo(final String response) {
        final Document doc;
        final Book book = new Book();
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(response));
            doc = documentBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();

            //description
            book.setIsbn(doc.getElementsByTagName("isbn13").item(0).getChildNodes().item(0).getNodeValue());
            book.setDescription(doc.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue());

            return book;
        } catch (SAXException | ParserConfigurationException | IOException pE) {
            pE.printStackTrace();
        }
        return null;
    }

    public List<Book> parseSearch(final String response) {
        final List<Book> books = new ArrayList<>();
        final Collection<Author> authors = new ArrayList<>();
        final Document doc;
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            final InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(response));
            doc = documentBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();
            final NodeList workNodes = doc.getElementsByTagName("work");
            for (int i = 0; i < workNodes.getLength(); i++) {
                final Book book = new Book();
                final Author author = new Author();
                final Element work = (Element) workNodes.item(i);
                final NodeList bestBook = work.getElementsByTagName("best_book");
                for (int j = 0; j < bestBook.getLength(); j++) {//simple cases one author< one image
                    final Element bestBookNode = (Element) bestBook.item(j);
                    book.setRating(Float.parseFloat(work.getElementsByTagName("average_rating").item(j).getChildNodes()
                            .item(0).getNodeValue()));
                    book.setTitle(bestBookNode.getElementsByTagName("title").item(0).getChildNodes().item(0)
                            .getNodeValue());
                    book.setId(Long.parseLong(bestBookNode.getElementsByTagName("id").item(0).getChildNodes().item(0)
                            .getNodeValue()));
                    final Element authorNode = (Element) bestBookNode.getElementsByTagName("author").item(0);
                    author.setName(authorNode.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
                    author.setId(Long.parseLong(authorNode.getElementsByTagName("id").item(0).getFirstChild()
                            .getNodeValue()));
                    book.setImage(bestBookNode.getElementsByTagName("image_url").item(0).getChildNodes().item(0)
                            .getNodeValue());

                    book.setAuthorId(author);
                }

                books.add(book);
                authors.add(author);
            }
        /*    new Thread(new Runnable() {
                @Override
                public void run() {
                    List<ContentValues> values = new ArrayList<>();
                    for (Book book : books) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("id", book.getId());
                        contentValues.put("title", book.getTitle());
                        contentValues.put("image", book.getImage());
                        contentValues.put("authorId", book.getAuthorId().getId());
                        values.add(contentValues);
                    }
                    mDBHelper.bulkInsert(Book.class, values);
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<ContentValues> values = new ArrayList<>();
                    for (Author author : authors) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("id", author.getId());
                        contentValues.put("name", author.getName());
                        values.add(contentValues);
                    }
                    mDBHelper.bulkInsert(Author.class, values);
                }
            }).start();
*/
            return books;
        } catch (ParserConfigurationException | SAXException | IOException pE) {
            pE.printStackTrace();
        }
        return null;

    }

}
