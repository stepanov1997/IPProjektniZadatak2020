package model.dto;

import java.util.Arrays;
import java.util.Objects;

public class Picture {
    private int id;
    private String fileName;
    private byte[] img;

    public Picture() {
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

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picture)) return false;
        Picture picture = (Picture) o;
        return getId() == picture.getId() &&
                Objects.equals(getFileName(), picture.getFileName()) &&
                Arrays.equals(getImg(), picture.getImg());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getFileName());
        result = 31 * result + Arrays.hashCode(getImg());
        return result;
    }
}
