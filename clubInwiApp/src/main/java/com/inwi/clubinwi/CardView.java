package com.inwi.clubinwi;

public class CardView {
    private int mImageResource;
    private String mText1, mText2;

    public CardView(int imageResource, String text1, String text2) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }
    public int getImage() {
        return mImageResource;
    }
    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }
}
