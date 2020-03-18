package net.etfbl.lab.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@ManagedBean(name = "fileUpload")
@ViewScoped
public class FileUploadBean {

	private Part uploadedFile;
	private String folder = "c:\\uploads";

	public Part getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public void saveFile() {

		try (InputStream input = uploadedFile.getInputStream()) {
			String fileName = uploadedFile.getName();
			Files.copy(input, new File(folder, fileName).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}