package com.example.ajax.myapplication;

import android.util.Log;

import com.example.ajax.myapplication.model.entity.Book;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Ajax on 14.10.2016.
 */

public class XMLHelper {

    public List<Book> parse(String response) {
        List<Book> books = new ArrayList<Book>();
        Document doc;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(response));
            doc = documentBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();
            NodeList workNodes = doc.getElementsByTagName("work");
            for (int i = 1; i < workNodes.getLength(); i++) {
                Book book = new Book();
                Element work = (Element) workNodes.item(i);
                NodeList bestBook = work.getElementsByTagName("best_book");
                for (int j = 0; j < bestBook.getLength(); j++) {//simple cases one author< one image
                    Element bestBookNode = (Element) bestBook.item(j);
                    NodeList title = bestBookNode.getElementsByTagName("title");
                    book.setTitle(title.item(0).getChildNodes().item(0).getNodeValue());
                    NodeList authors = bestBookNode.getElementsByTagName("author");
                    Element author = (Element) authors.item(0);
                    NodeList name = author.getElementsByTagName("name");
                    Element nameNode = (Element) name.item(0);
                    book.setAuthor(nameNode.getChildNodes().item(0).getNodeValue());
                    NodeList links = bestBookNode.getElementsByTagName("small_image_url");
                    Element linkNode = (Element) links.item(0);
                    book.setSmallImage(linkNode.getChildNodes().item(0).getNodeValue());
                }
                books.add(book);
            }

            return books;
        } catch (Exception e) {
            Log.d("Exception",e.getMessage());
        }
        return null;

    }

}
