package model.dto;

import java.util.Arrays;
import java.util.Objects;

public class Video {
    private int id;
    private String fileName;
    private byte[] video;

    public Video() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Video)) return false;
        Video video1 = (Video) o;
        return getId() == video1.getId() &&
                Objects.equals(getFileName(), video1.getFileName()) &&
                Arrays.equals(getVideo(), video1.getVideo());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getFileName());
        result = 31 * result + Arrays.hashCode(getVideo());
        return result;
    }
}
