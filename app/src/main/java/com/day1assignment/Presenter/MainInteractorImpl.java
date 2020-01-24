package com.day1assignment.Presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.day1assignment.Interface.GetDataListener;
import com.day1assignment.Interface.MainInteractor;
import com.day1assignment.Interface.MainView;
import com.day1assignment.Model.CardModelClass;
import com.day1assignment.R;
import com.day1assignment.Util.API;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainInteractorImpl implements MainInteractor {

    private GetDataListener mGetDatalistener;
    private MainView mainView;
    private RSSHandler rssHandler;

    MainInteractorImpl(GetDataListener mGetDatalistener) {
        this.mGetDatalistener = mGetDatalistener;

    }

    @Override
    public void provideData(Context context) {

        if (isInternetOn(context)) {
            this.initNetworkCall(context);
        } else {

            mGetDatalistener.onFailure("No internet connection.");
        }

    }

    private void initNetworkCall(Context context) {

        try {

            URL rssUrl = new URL(API.ASS_URL);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            rssHandler = new RSSHandler();
            xmlReader.setContentHandler(rssHandler);
            InputSource inputSource = new InputSource(rssUrl.openStream());
            xmlReader.parse(inputSource);

        } catch (IOException | SAXException e) {
            mGetDatalistener.onFailure(e.toString());
        } catch (ParserConfigurationException e) {
            mGetDatalistener.onFailure(e.toString());


        }

        mGetDatalistener.onSuccess(context.getString(R.string.success), rssHandler.getItems());
    }


    private boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                final Network network = cm.getActiveNetwork();

                if (network != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(network);

                    assert nc != null;
                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            }
        }

        return false;
    }

    private class RSSHandler extends DefaultHandler {
        private List<CardModelClass> cardModelClassList;
        boolean parsingPubDat = false, parsingTitle = false, parsingCat = false, parsingDes = false;
        private CardModelClass rssFeed;
        private boolean parsingLink;
        String rssResult = "";
        boolean item = false;

        RSSHandler() {
            cardModelClassList = new ArrayList();
        }

        List<CardModelClass> getItems() {
            return cardModelClassList;
        }

        public void startElement(String uri, String localName, String qName,
                                 Attributes attrs) {

            if (localName.equalsIgnoreCase("item")) {
                rssFeed = new CardModelClass();
                item = true;
            } else if (localName.equalsIgnoreCase("title")) {
                parsingTitle = true;
            } else if (localName.equalsIgnoreCase("link")) {
                parsingLink = true;
            } else if (localName.equalsIgnoreCase("category")) {
                parsingCat = true;
            } else if (localName.equalsIgnoreCase("pubDate")) {
                parsingPubDat = true;
            } else if (localName.equalsIgnoreCase("description")) {
                parsingDes = true;
            }


            if (!localName.equals("item") && item) {
                rssResult = rssResult + localName + ": ";
                if (localName.equalsIgnoreCase("description")) {
                    parsingDes = true;
                }

            }

        }

        public void endElement(String namespaceURI, String localName,
                               String qName) {
            if (localName.equalsIgnoreCase("item")) {
                cardModelClassList.add(rssFeed);
                rssFeed = null;
            }

            if (localName.equalsIgnoreCase("title")) {
                parsingTitle = false;
            } else if (localName.equalsIgnoreCase("link")) {
                parsingLink = false;
            } else if (localName.equalsIgnoreCase("category")) {
                parsingCat = false;
            } else if (localName.equalsIgnoreCase("pubDate")) {
                parsingPubDat = false;
            } else if (localName.equalsIgnoreCase("description")) {
                parsingDes = false;
            }

        }

        public void characters(char[] ch, int start, int length) {
            String cdata = new String(ch, start, length);
            if (item) {
                if (parsingTitle) {
                    if (rssFeed != null) {
                        rssFeed.setTitle(cdata);

                    }
                } else if (parsingLink) {
                    if (rssFeed != null) {
                        rssFeed.setLink(cdata);
                        parsingLink = false;
                    }
                } else if (parsingCat) {
                    if (rssFeed != null) {
                        rssFeed.setCat(cdata);
                        parsingCat = false;
                    }
                } else if (parsingDes) {
                    if (rssFeed != null) {
                        rssFeed.setDescription(cdata);
                        Log.d("Result111", (cdata.trim()).replaceAll("\\s+", ""));

                        parsingDes = false;
                    }
                } else if (parsingPubDat) {
                    if (rssFeed != null) {
                        rssFeed.setPubdate(cdata);
                        parsingPubDat = false;
                    }
                }
            }

        }

    }

}
