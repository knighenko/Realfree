 package com.knighenko.realfree.model;

public enum UrlOfPages {
    HOME_GARDEN("https://www.olx.ua/dom-i-sad/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=15", "Дом и сад"),
    ELECTRONICS("https://www.olx.ua/elektronika/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=15", "Электроника"),
    TRANSPORT_PARTS("https://www.olx.ua/zapchasti-dlya-transporta/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=15", "Запчасти к транспорту"),
    BUSINESS_AND_SERVICES("https://www.olx.ua/uslugi/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=15", "Бизнес и услуги"),
    FASHION_AND_STYLE("https://www.olx.ua/moda-i-stil/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=15", "Мода и стиль"),
    HOBBIES_AND_LEISURE("https://www.olx.ua/hobbi-otdyh-i-sport/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=15", "Хобби и досуг"),
    WORLD_OF_CHILDREN("https://www.olx.ua/detskiy-mir/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=15", "Детский мир"),
    ZOOPRODUCT("https://www.olx.ua/uk/zhivotnye/tovary-dlya-zhivotnyh/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=50", "Зоотовары"),
    ARISTON("https://www.olx.ua/kha/q-ariston-%D0%B1%D0%BE%D0%B9%D0%BB%D0%B5%D1%80/?search%5Bfilter_float_price%3Ato%5D=1000", "Аристон");

    private String url;
    private String title;

    UrlOfPages(String url, String title) {
        this.url = url;this.title=title;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
