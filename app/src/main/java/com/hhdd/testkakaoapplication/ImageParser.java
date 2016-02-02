package com.hhdd.testkakaoapplication;

import android.os.Handler;

import com.hhdd.testkakaoapplication.event.ImageParserListener;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heedeok on 2016. 2. 2..
 */
public class ImageParser {
    private static final String BASE_DIR = "http://www.gettyimagesgallery.com";
    private static final String DOCUMENT_URL = BASE_DIR+"/collections/archive/slim-aarons.aspx";
    private static Handler mHandler;
    private ImageParserListener mListener;

    public ImageParser()
    {
        mHandler = new Handler();
    }

    public void setListener(ImageParserListener listener)
    {
        mListener = listener;
    }

    public ImageParserListener getListener()
    {
        return mListener;
    }

    public void getImageLists()
    {
        new Thread() {
            public void run() {
                final ArrayList<String> lists = new ArrayList<String>();
                try
                {
                    URL url = new URL(DOCUMENT_URL);
                    URLConnection connection;
                    connection = url.openConnection();

                    HttpURLConnection httpconnection = (HttpURLConnection)connection;

                    int responseCode = httpconnection.getResponseCode();

                    if(responseCode == HttpURLConnection.HTTP_OK)
                    {
                        InputStream in = httpconnection.getInputStream();
                        Source src = new Source(new InputStreamReader(in, "UTF-8"));
                        src.fullSequentialParse();

                        List<StartTag> imgtags = src.getAllStartTags(HTMLElementName.IMG);
                        for(StartTag tag : imgtags)
                        {
                            String path = tag.getAttributeValue("src");
                            if(path.substring(0, 7).equals("http://") == false) {
                                path = BASE_DIR + path;
                            }
                            lists.add(path);
                        }
                    }
                }
                catch(Exception e){
                    mHandler.post(new Runnable() {
                        public void run() {
                            mListener.onFault(0);
                        }
                    });

                    return;
                }

                mHandler.post(new Runnable() {
                    public void run() {
                        mListener.onResult(lists);
                    }
                });
            }
        }.start();
    }
}
