package com.example.newskribble;

import java.util.ArrayList;

public class MusicModel {

    public ArrayList<MusicModel.data> getData() {
        return data;
    }

    public void setData(ArrayList<MusicModel.data> data) {
        this.data = data;
    }

    ArrayList<data> data;

    public class data{
        String id;
        String title;
        int duration;
        String preview;




        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }
    }


}
