/**
 * Copyright (C) 2010 Hybitz.co.ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * 
 */
package jp.co.hybitz.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class StreamUtils {

    public static void write(InputStream is, OutputStream os) throws IOException {
        write(is, os, true);
    }
    
    public static void write(InputStream is, OutputStream os, boolean closeStream) throws IOException {
        int count = 0;
        byte b[] = new byte[4096];
        try {
            while ((count = is.read(b)) != -1) {
                os.write(b, 0, count);
            }
        }
        finally {
            if (closeStream) {
                try {
                    is.close();
                }
                catch (IOException e) {
                }
                try {
                    os.close();
                }
                catch (IOException e) {
                }
            }
        }
    }
}
