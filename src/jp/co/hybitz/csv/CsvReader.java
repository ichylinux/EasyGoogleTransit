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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class CsvReader {

    private List<String> row = new ArrayList<String>();
    private String lineSeparator;
    private char delimiter;
    private BufferedReader reader;
    private boolean maybeEmptyString;

    /**
     * コンストラクタ
     */
    public CsvReader(String s) throws IOException {
        this(new StringReader(s));
    }

    /**
     * コンストラクタ
     */
    public CsvReader(File file) throws IOException {
        this(new FileReader(file));
    }

    /**
     * コンストラクタ
     */
    public CsvReader(InputStream is, String encoding) throws IOException {
        this(is, encoding, ',', System.getProperty("line.separator"));
    }

    /**
     * コンストラクタ
     */
    public CsvReader(Reader reader) throws IOException {
        this(reader, ',', System.getProperty("line.separator"));
    }

    /**
     * コンストラクタ
     */
    public CsvReader(InputStream is, String encoding, char delimiter) throws IOException {
        this(is, encoding, delimiter, System.getProperty("line.separator"));
    }

    /**
     * コンストラクタ
     */
    public CsvReader(InputStream is, String encoding, char delimiter, String lineSeparator) throws IOException {
        this(new InputStreamReader(is, encoding), delimiter, lineSeparator);
    }

    /**
     * コンストラクタ
     */
    public CsvReader(Reader reader, char delimiter, String lineSeparator) {
        this.delimiter = delimiter;
        this.lineSeparator = lineSeparator;

        if (reader instanceof BufferedReader) {
            this.reader = (BufferedReader) reader;
        } else {
            this.reader = new BufferedReader(reader);
        }
    }

    /**
     * CSVをパースし、1行分のデータを返します。
     * CSVストリームの最後まで達した場合はストリームをクローズしてからnullを返します。
     * 
     * @throws CsvException CSVの書式が不正な場合
     * @throws IOException 
     */
    public String[] read() throws CsvException, IOException {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                // 新しい行開始
                row.clear();
                StringBuilder buffer = new StringBuilder();
                maybeEmptyString = false;

                // 各行を解析
                boolean inQuote = parseLine(line, buffer, false);
                while (inQuote) {
                    if ((line = reader.readLine()) == null) {
                        throw new CsvException("解析途中にファイルの最後に到達しました。");
                    }
                    inQuote = parseLine(line, buffer, true);
                }

                // １行分のデータ読み込み完了
                return row.toArray(new String[0]);
            }
        } catch (IOException e) {
            closeStream();
            throw e;
        } catch (CsvException e) {
            closeStream();
            throw e;
        }

        closeStream();
        return null;
    }

    private void closeStream() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
        } finally {
            reader = null;
        }
    }

    private boolean parseLine(String line, StringBuilder buffer, boolean inQuote) throws CsvException {
        int len = line.length();

        for (int i = 0; i < len; i++) {
            // ダブルクオートの場合
            if (line.charAt(i) == '"') {
                if (inQuote) {
                    // 行の最後なら、クオート終了
                    if (i == len - 1) {
                        inQuote = false;
                    } else {
                        char next = line.charAt(i + 1);

                        // 次がダブルクオートなら、クオート内でのエスケープ
                        if (next == '"') {
                            buffer.append('"');
                            i++;
                        } // 次がデリミタなら、クオート終了
                        else if (next == delimiter) {
                            inQuote = false;
                        } else {
                            throw new CsvException("ダブルクオートが不正な位置にあります。");
                        }
                    }
                } else {
                    // 列の最初なら、クオート開始
                    if (buffer.length() == 0) {
                        inQuote = true;
                        maybeEmptyString = true;
                    } else {
                        throw new CsvException("ダブルクオートが不正な位置にあります。");
                    }
                }
            } // デリミタの場合
            else if (line.charAt(i) == delimiter) {
                // ダブルクオート内だとデータの一部
                if (inQuote) {
                    buffer.append(delimiter);
                } else {
                    flushBuffer(buffer);
                }
            } // 通常の文字の場合
            else {
                buffer.append(line.charAt(i));
            }
        }

        if (inQuote) {
            buffer.append(lineSeparator);
        } else {
            flushBuffer(buffer);
        }

        return inQuote;
    }

    private void flushBuffer(StringBuilder buffer) {
        if (buffer.length() == 0) {
            if (maybeEmptyString) {
                row.add("");
                maybeEmptyString = false;
            } else {
                row.add(null);
            }
        } else {
            row.add(buffer.toString());
        }
        buffer.setLength(0);
    }

    /**
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeStream();
    }
}
