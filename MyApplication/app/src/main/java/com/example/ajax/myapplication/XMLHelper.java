package com.example.ajax.myapplication;

import android.util.Log;

import com.example.ajax.myapplication.database.DBHelper;
import com.example.ajax.myapplication.model.entity.Author;
import com.example.ajax.myapplication.model.entity.Book;
import com.example.ajax.myapplication.utils.ContextHolder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

final class XMLHelper {

    private static final XMLHelper instance = new XMLHelper();
    private final DBHelper mDBHelper;

    private XMLHelper() {
        mDBHelper = new DBHelper(ContextHolder.get(), null, 1);
    }

    public static XMLHelper getInstance() {
        return instance;
    }

    public List<Book> parse(final String response) {
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
                book.setRating(Float.parseFloat(work.getElementsByTagName("average_rating").item(0).getChildNodes()
                        .item(0).getNodeValue()));
                final NodeList bestBook = work.getElementsByTagName("best_book");
                for (int j = 0; j < bestBook.getLength(); j++) {//simple cases one author< one image
                    final Element bestBookNode = (Element) bestBook.item(j);

                    book.setTitle(bestBookNode.getElementsByTagName("title").item(0).getChildNodes().item(0)
                            .getNodeValue());
                    book.setId(Long.parseLong(bestBookNode.getElementsByTagName("id").item(0).getChildNodes().item(0)
                            .getNodeValue()));
                    final Element authorXML = (Element) bestBookNode.getElementsByTagName("author").item(0);
                    author.setName(authorXML.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
                    author.setId(Long.parseLong(authorXML.getElementsByTagName("id").item(0).getFirstChild()
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
        } catch (final Exception e) {
            Log.d("Exception", e.getMessage());
        }
        return null;

    }

}
