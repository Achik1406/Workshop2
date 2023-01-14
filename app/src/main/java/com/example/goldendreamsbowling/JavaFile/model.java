package com.example.goldendreamsbowling.JavaFile;

public class model
{
  String PromoCode,PromoValue;

    public model() {
    }

    public model(String promoCode, String promoValue) {
        PromoCode = promoCode;
        PromoValue = promoValue;
    }

    public String getPromoCode() {
        return PromoCode;
    }

    public void setPromoCode(String promoCode) {
        PromoCode = promoCode;
    }

    public String getPromoValue() {
        return PromoValue;
    }

    public void setPromoValue(String promoValue) {
        PromoValue = promoValue;
    }
}
