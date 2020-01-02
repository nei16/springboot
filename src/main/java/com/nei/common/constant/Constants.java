package com.nei.common.constant;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Created by bo27.li on 2018/10/17 18:29.
 */
public interface Constants {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    FastDateFormat DEFAULT_DATE_FORMAT = FastDateFormat.getInstance(DEFAULT_DATE_PATTERN);

    String EMPTY = "";

    String COMMA = ",";

    String LINE_THROUGH = "-";

    String UNDERLINE = "_";

}
