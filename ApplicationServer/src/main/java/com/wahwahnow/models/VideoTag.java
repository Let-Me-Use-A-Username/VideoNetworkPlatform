package com.wahwahnow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table
@Entity(name = "video_tag")
public class VideoTag implements Serializable {

    @Id
    private int id;
    @Column(columnDefinition = "TEXT")
    private String name;

    public VideoTag(){ }

    public VideoTag(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
