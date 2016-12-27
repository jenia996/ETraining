package com.example.ajax.myapplication.download.operations;

import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.model.viewmodel.AuthorModel;
import com.example.ajax.myapplication.utils.XMLParser;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class ParseAuthorOperation implements OwnAsyncTask<String, Void, AuthorModel> {

    @Override
    public AuthorModel doInBackground(final String pResult, final ProgressCallback<Void> progressCallback) throws
            IOException, ParserConfigurationException, SAXException {
        return XMLParser.parseAuthorBooks(pResult);
    }
}
