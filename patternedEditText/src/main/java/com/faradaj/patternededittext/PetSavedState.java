package com.faradaj.patternededittext;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class PetSavedState extends View.BaseSavedState implements Parcelable {

    private String mRawText;

    PetSavedState(Parcelable superState) {
        super(superState);
    }

    protected PetSavedState(Parcel source) {
        super(source);
        mRawText = source.readString();
    }

    protected PetSavedState(Parcelable superState, String rawText) {
        super(superState);
        mRawText = rawText;
    }

    public void setRealText(String s) {
        mRawText = s ;
    }

    public String getRealText() {
        return mRawText;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        super.writeToParcel(destination, flags);
        destination.writeString(mRawText);
    }

    public static final Parcelable.Creator<PetSavedState> CREATOR = new Parcelable.Creator<PetSavedState>() {
        @Override
        public PetSavedState createFromParcel(Parcel source) {
            return new PetSavedState(source);
        }

        @Override
        public PetSavedState[] newArray(int size) {
            return new PetSavedState[size];
        }
    };


    @Override
    public String toString() {
        return "PetSavedState{" +
                "mRawText='" + mRawText + '\'' +
                '}';
    }
}
