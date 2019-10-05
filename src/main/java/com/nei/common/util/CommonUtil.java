package com.nei.common.util;

import java.util.Date;
import java.util.UUID;

import static com.nei.common.constant.Constants.DEFAULT_DATE_FORMAT;
import static com.nei.common.constant.Constants.EMPTY;
import static com.nei.common.constant.Constants.LINE_THROUGH;

public class CommonUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll(LINE_THROUGH, EMPTY);
    }

    public static String now() {
        return DEFAULT_DATE_FORMAT.format(new Date());
    }

}
