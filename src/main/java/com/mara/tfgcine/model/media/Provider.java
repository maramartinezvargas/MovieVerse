package com.mara.tfgcine.model.media;

public class Provider {
    private String name;
    private String logoPath;
    private String link;
    private int providerId;

    public Provider(int providerId, String name, String logoPath, String link) {
        this.providerId = providerId;
        this.name = name;
        this.logoPath = logoPath;
        this.link = link;
    }

    public String getName() { return name; }
    public String getLogoPath() { return logoPath; }
    public String getLink() { return link; }
    public int getProviderId() {return providerId;}
}