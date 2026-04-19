package com.mara.tfgcine.model.media;

public class Provider {
    private String name;
    private String logoPath;

    public Provider(String name, String logoPath) {
        this.name = name;
        this.logoPath = logoPath;
    }

    public String getName() { return name; }
    public String getLogoPath() { return logoPath; }
}