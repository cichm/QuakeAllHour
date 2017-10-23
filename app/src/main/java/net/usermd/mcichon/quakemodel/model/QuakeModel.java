package net.usermd.mcichon.quakemodel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class QuakeModel {

    @Setter
    @Getter
    @SerializedName("features")
    @Expose
    public ArrayList<Features> features;

    public static class Features {

        @Setter
        @Getter
        @SerializedName("id")
        @Expose
        public String quake_id;

        @Setter
        @Getter
        @SerializedName("properties")
        @Expose
        public Properties properties;

        @Setter
        @Getter
        @SerializedName("geometry")
        @Expose
        public Geometry geometry;

        public static class Properties {

            @Setter
            @Getter
            @SerializedName("mag")
            @Expose
            public float mag;

            @Setter
            @Getter
            @SerializedName("place")
            @Expose
            public String place;

            @Setter
            @Getter
            @SerializedName("url")
            @Expose
            public String url;

            @Setter
            @Getter
            @SerializedName("time")
            @Expose
            public float time;

            @Setter
            @Getter
            @SerializedName("title")
            @Expose
            public String title;
        }

        public static class Geometry {
            @Setter
            @Getter
            @SerializedName("coordinates")
            @Expose
            float[] coordinates;
        }
    }
}

