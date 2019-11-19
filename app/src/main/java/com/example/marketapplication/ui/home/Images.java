package com.example.marketapplication.ui.home;

public class Images
{
    private String price;
    private String url;

    public Images()
    {
    }

    public Images(String price, String url)
    {
        this.price = price;
        this.url = url;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
