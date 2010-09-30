package jp.co.hybitz.timetable;

public interface YahooConfig {
    static final String YAHOO_TRANSIT_URL = "http://transit.map.yahoo.co.jp/station/list";
    
    static final String[][] shonanShinjukuStationsSaitama = new String[][]{
        {"大宮(埼玉)",
            "http://transit.map.yahoo.co.jp/station/time/21987/?pref=11&company=JR&line=%E4%BA%AC%E6%B5%9C%E6%9D%B1%E5%8C%97%E7%B7%9A&prefname=%E5%9F%BC%E7%8E%89&gid=8"},
    };
    
    static final String[][] shonanShinjukuStationsTokyo = new String[][]{
        {"赤羽",
            "http://transit.map.yahoo.co.jp/station/time/22487/?pref=13&company=JR&line=%E4%BA%AC%E6%B5%9C%E6%9D%B1%E5%8C%97%E7%B7%9A&prefname=%E6%9D%B1%E4%BA%AC&gid=9"},
        {"池袋",
            "http://transit.map.yahoo.co.jp/station/time/22513/?pref=13&company=JR&line=%E5%B1%B1%E6%89%8B%E7%B7%9A&prefname=%E6%9D%B1%E4%BA%AC&gid=5"},
        {"新宿",
            "http://transit.map.yahoo.co.jp/station/time/22741/?pref=13&company=JR&line=%E5%B1%B1%E6%89%8B%E7%B7%9A&prefname=%E6%9D%B1%E4%BA%AC&gid=7"},
        {"渋谷",
            "http://transit.map.yahoo.co.jp/station/time/22715/?pref=13&company=JR&line=%E5%B1%B1%E6%89%8B%E7%B7%9A&prefname=%E6%9D%B1%E4%BA%AC&gid=5"},
        {"恵比寿",
            "http://transit.map.yahoo.co.jp/station/time/22548/?pref=13&company=JR&line=%E5%B1%B1%E6%89%8B%E7%B7%9A&prefname=%E6%9D%B1%E4%BA%AC&gid=5"},
        {"大崎",
            "http://transit.map.yahoo.co.jp/station/time/22559/?pref=13&company=JR&line=%E5%B1%B1%E6%89%8B%E7%B7%9A&prefname=%E6%9D%B1%E4%BA%AC&gid=4"},
        {"西大井",
            "http://transit.map.yahoo.co.jp/station/time/22865/?pref=13&company=JR&line=%E6%A8%AA%E9%A0%88%E8%B3%80%E7%B7%9A&prefname=%E6%9D%B1%E4%BA%AC&gid=3"},
    };
    
    static final String[][] shonanShinjukuStationsKanagawa = new String[][]{
        {"武蔵小杉",
            "http://transit.map.yahoo.co.jp/station/time/23345/?pref=14&company=JR&line=%E6%A8%AA%E9%A0%88%E8%B3%80%E7%B7%9A&prefname=%E7%A5%9E%E5%A5%88%E5%B7%9D&gid=5"},
        {"新川崎",
            "http://transit.map.yahoo.co.jp/station/time/23204/?pref=14&company=JR&line=%E6%A8%AA%E9%A0%88%E8%B3%80%E7%B7%9A&prefname=%E7%A5%9E%E5%A5%88%E5%B7%9D&gid=3"},
        {"横浜",
            "http://transit.map.yahoo.co.jp/station/time/23368/?pref=14&company=JR&line=%E6%A8%AA%E9%A0%88%E8%B3%80%E7%B7%9A&prefname=%E7%A5%9E%E5%A5%88%E5%B7%9D&gid=8"},
        {"保土ケ谷",
            "http://transit.map.yahoo.co.jp/station/time/23316/?pref=14&company=JR&line=%E6%A8%AA%E9%A0%88%E8%B3%80%E7%B7%9A&prefname=%E7%A5%9E%E5%A5%88%E5%B7%9D&gid=3"},
        {"東戸塚",
            "http://transit.map.yahoo.co.jp/station/time/23291/?pref=14&company=JR&line=%E6%A8%AA%E9%A0%88%E8%B3%80%E7%B7%9A&prefname=%E7%A5%9E%E5%A5%88%E5%B7%9D&gid=3"},
        {"戸塚",
            "http://transit.map.yahoo.co.jp/station/time/23251/?pref=14&company=JR&line=%E6%A8%AA%E9%A0%88%E8%B3%80%E7%B7%9A&prefname=%E7%A5%9E%E5%A5%88%E5%B7%9D&gid=5"},
        {"大船",
            "http://transit.map.yahoo.co.jp/station/time/23096/?pref=14&company=JR&line=%E6%A8%AA%E9%A0%88%E8%B3%80%E7%B7%9A&prefname=%E7%A5%9E%E5%A5%88%E5%B7%9D&gid=6"},
    };
    
    static final String[][] gidList = new String[][]{
        {"京浜急行電鉄", "本線", "横浜", "11"},
        {"京浜急行電鉄", "本線", "品川", "13"},
        {"相模鉄道", "本線", "横浜", "13"},
        {"横浜市交通局", "３号線", "横浜", "14"},
    };

    static final String[][] extraGidList = new String[][]{
        {"JR", "総武線", "新小岩", "3,4"},
        {"JR", "総武線", "錦糸町", "3,4"},
        {"JR", "総武線", "千葉", "6"},
        {"JR", "総武線", "稲毛", "3,4"},
        {"JR", "総武線", "津田沼", "3,4"},
        {"JR", "総武線", "船橋", "3,4"},
        {"JR", "総武線", "市川", "3,4"},
        {"JR", "中央線", "御茶ノ水", "3,4"},
        {"JR", "中央線", "四ツ谷", "3,4"},
        {"JR", "中央線", "新宿", "9,10"},
        {"JR", "中央線", "中野(東京都)", "3,4"},
        {"JR", "中央線", "高円寺", "3,4"},
        {"JR", "中央線", "阿佐ケ谷", "3,4"},
        {"JR", "中央線", "荻窪", "3,4"},
        {"JR", "中央線", "西荻窪", "3,4"},
        {"JR", "中央線", "吉祥寺", "3,4"},
        {"JR", "中央線", "三鷹", "3"},
        {"北総鉄道", "北総線", "京成高砂", "5"},
        {"北総鉄道", "北総線", "東松戸", "4,5"},
        {"北総鉄道", "北総線", "新鎌ケ谷", "6,7"},
        {"北総鉄道", "北総線", "千葉ニュータウン中央", "2,3"},
        {"北総鉄道", "北総線", "印旛日本医大", "2"},
        {"京成電鉄", "本線", "空港第２ビル(鉄道)", "5,6"},
        {"京成電鉄", "本線", "成田空港(鉄道)", "3"},
    };

    static final String[][] lineMappings = new String[][]{
        {"横浜市交通局", "１号線", "ブルーライン"},
        {"横浜市交通局", "３号線", "ブルーライン"},
        {"横浜市交通局", "４号線", "グリーンライン"},
        {"千葉都市モノレール", "１号線", "モノレール"},
        {"千葉都市モノレール", "２号線", "モノレール"},
    };

}
