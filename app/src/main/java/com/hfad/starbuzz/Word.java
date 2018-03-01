package com.hfad.starbuzz;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Parcelable {
    private int wordId;
    private String wordStr;
    private String meaning;
    private String topic;
    private int rating;

    public Word() {
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getWordStr() {
        return wordStr;
    }

    public void setWordStr(String word) {
        this.wordStr = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(wordId);
        parcel.writeString(wordStr);
        parcel.writeString(meaning);
        parcel.writeString(topic);
        parcel.writeInt(rating);
    }

    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    private Word(Parcel in) {
        setWordId(in.readInt());
        setWordStr(in.readString());
        setMeaning(in.readString());
        setTopic(in.readString());
        setRating(in.readInt());
    }
}
