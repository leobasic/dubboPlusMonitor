/**
 * Copyright 2006-2015 handu.com
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.handu.open.dubbo.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

/**
 * CountUtils
 *
 * @author Jinkai.Ma
 */
public final class CountUtils {

    private static final Logger logger = LoggerFactory.getLogger(CountUtils.class);

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    private static final int SUM = 0;

    private static final int MAX = 1;

    private static final int AVG = 2;

    public static long sum(File file) {
        return calc(file, SUM);
    }

    public static long max(File file) {
        return calc(file, MAX);
    }

    public static long avg(File file) {
        return calc(file, AVG);
    }

    private static long calc(File file, int op) {
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                try {
                    int times = 0;
                    int count = 0;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int i = line.indexOf(" ");
                        if (i > 0) {
                            line = line.substring(i + 1).trim();
                            if (NUMBER_PATTERN.matcher(line).matches()) {
                                int value = Integer.parseInt(line);
                                times++;
                                if (op == MAX) {
                                    count = Math.max(count, value);
                                } else {
                                    count += value;
                                }
                            }
                        }
                    }
                    if (op == AVG) {
                        return count / times;
                    }
                    return count;
                } finally {
                    reader.close();
                }
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        return 0;
    }
    
    public static void main(String args[]) throws UnsupportedEncodingException
    {
    	System.out.println(

    			 URLDecoder.decode("哈%24哦%25哈", "UTF-8")
    			
    			);
    }
    

}