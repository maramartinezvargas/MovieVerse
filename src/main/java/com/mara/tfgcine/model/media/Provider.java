package com.mara.tfgcine.model.media;

public class Provider {
    private String name;
    private String logoPath;
    private String link;

    public Provider(String name, String logoPath, String link) {
        this.name = name;
        this.logoPath = logoPath;
        this.link = link;
    }

    public String getName() { return name; }
    public String getLogoPath() { return logoPath; }
    public String getLink() { return link; }
}