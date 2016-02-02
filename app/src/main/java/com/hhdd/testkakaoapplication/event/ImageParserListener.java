package com.hhdd.testkakaoapplication.event;

import java.util.ArrayList;

/**
 * Created by Heedeok on 2016. 2. 2..
 */
public interface ImageParserListener {
    void onResult(ArrayList<String> list);
    void onFault(int code);
}
