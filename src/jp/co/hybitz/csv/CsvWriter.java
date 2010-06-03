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
package jp.co.hybitz.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class CsvWriter {
    private String lineSeparator;
    private char delimiter;
    private BufferedWriter writer;
    private boolean writeCalled = false;

    /**
     * コンストラクタ
     */
    public CsvWriter( OutputStream os, String encoding ) throws IOException {
        this(os, encoding, ',', System.getProperty("line.separator"));
    }

    /**
     * コンストラクタ
     */
    public CsvWriter( OutputStream os, String encoding, char delimiter ) throws IOException {
        this(os, encoding, delimiter, System.getProperty("line.separator"));
    }

    /**
     * コンストラクタ
     */
    public CsvWriter( OutputStream os, String encoding, String lineSeparator ) throws IOException {
        this(os, encoding, ',', lineSeparator);
    }

    /**
     * コンストラクタ
     */
    public CsvWriter( OutputStream os, String encoding, char delimiter, String lineSeparator ) throws IOException {
        this(new OutputStreamWriter( os, encoding ), delimiter, lineSeparator);
    }

    /**
     * コンストラクタ
     */
    public CsvWriter( Writer writer ) {
        this(writer, ',', System.getProperty("line.separator"));
    }
    
    /**
     * コンストラクタ
     */
    public CsvWriter( Writer writer, char delimiter ) {
        this(writer, delimiter, System.getProperty("line.separator"));
    }

    /**
     * コンストラクタ
     */
    public CsvWriter( Writer writer, char delimiter, String lineSeparator ) {
        this.delimiter = delimiter;
        this.lineSeparator = lineSeparator;
        if ( writer instanceof BufferedWriter ) {
            this.writer = (BufferedWriter) writer;
        } else {
            this.writer = new BufferedWriter( writer );
        }
    }

    /**
     * CSVを書き込みます。
     *
     * @param line 1行分のデータ
     * @throws IOException
     */
    public void write(String[] line) throws IOException {
        write(line, false);
    }
    
    public void writeAndClose(String[] line) throws IOException {
        write(line, true);
    }
    
    public static String toString(String[] line) {
        try {
            StringWriter sw = new StringWriter();
            CsvWriter cw = new CsvWriter(sw);
            cw.writeAndClose(line);
            return sw.toString();
        } catch (IOException e) {
        }
        
        return null;
    }

    /**
     * CSV1行を書き込みます。
     *
     * @param line 1行分のデータ
     * @param closeStream 書き込み後ストリームを閉じるかどうか
     * @throws IOException
     */
    private void write(String[] line, boolean closeStream) throws IOException {
        try {
            if ( writeCalled ) {
                writer.write(lineSeparator);
            }
            else {
                writeCalled = true;
            }

            for (int i = 0; i < line.length; i ++ ) {
                String value = escape(line[i], delimiter, lineSeparator);

                if ( i != 0 ) {
                    writer.write(delimiter);
                }

                if (value != null) {
                    writer.write(value);
                }
            }

        } catch ( IOException e ) {
            close();
            throw e;
        }

        if ( closeStream ) {
            close();
        }
    }
    
    /**
     * ストリームを閉じます。
     */
    public void close() {
        try { if ( writer != null ) writer.close(); } catch ( IOException e ) {}
    }

    protected String escape( String value, char delimiter, String lineSeparator ) {
        if ( value == null ) return null;
        else if ( value.length() == 0 ) return "\"\"";

        if ( value.indexOf('"') >= 0 || value.indexOf( delimiter ) >= 0 || value.indexOf( lineSeparator ) >= 0 ) {
            StringBuilder sb = new StringBuilder();
            sb.append("\"");
            sb.append( value.replaceAll("\"", "\"\"") );
            sb.append("\"");
            return sb.toString();
        }
        else {
            return value;
        }
    }
}
