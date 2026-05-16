package com.mara.tfgcine.model.dto;

import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.status.MediaStatus;

public class StatusRequestDTO {

    private Long mediaId;

    private MediaType mediaType;

    private MediaStatus status;

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }

}