package com.nei.common.util;

import com.nei.common.constant.Constants;

import java.util.Date;
import java.util.UUID;

public class CommonUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String now() {
        return Constants.DEFAULT_DATE_FORMAT.format(new Date());
    }

}
