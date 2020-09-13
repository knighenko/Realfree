package com.knighenko.realfree.model;

public enum UrlOfPages {
    HOME_GARDEN("https://www.olx.ua/dom-i-sad/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=10"),
    ELECTRONICS("https://www.olx.ua/elektronika/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=10"),
    TRANSPORT_PARTS("https://www.olx.ua/zapchasti-dlya-transporta/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=10"),
    BUSINESS_AND_SERVICES("https://www.olx.ua/uslugi/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=10"),
    FASHION_AND_STYLE("https://www.olx.ua/moda-i-stil/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=10"),
    HOBBIES_AND_LEISURE("https://www.olx.ua/hobbi-otdyh-i-sport/kharkov/?search%5Bfilter_float_price%3Afrom%5D=free&search%5Bdist%5D=10");

    private String url;

    UrlOfPages(String s) {
        this.url = s;
    }

    public String getUrl(){
        return url;
    }
}
