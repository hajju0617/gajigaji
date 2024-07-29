package com.green.project2nd.common.model;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
@Getter
public class CustomFileUtils {

    public final String uploadPath;

    public CustomFileUtils(@Value("${file.directory}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String makeFolders(String path) {
        File folder = new File(uploadPath, path);
        folder.mkdirs();
        return folder.getAbsolutePath();
    }


    public String getExt(String fileName) {
        int idx = fileName.lastIndexOf(".");
        return fileName.substring(idx);
    }


    public String makeRandomFileName() {
        return UUID.randomUUID().toString();
    }


    public String makeRandomFileName(String fileName) {
        return makeRandomFileName() + getExt(fileName);
    }


    public String makeRandomFileName(MultipartFile mf) {
        return mf == null ? null : makeRandomFileName(mf.getOriginalFilename());
    }


    public void transferTo(MultipartFile mf, String target) throws Exception {
        File saveFile = new File(uploadPath, target); //최종 경로
        mf.transferTo(saveFile);
    }

    public void deleteFolder(String absoluteFolderPath) {
        File folder = new File(absoluteFolderPath);
        if(folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            for(File f : files) {
                if(f.isDirectory()) {
                    deleteFolder(f.getAbsolutePath());
                } else {
                    f.delete();
                }
            }
            folder.delete();
        }
    }


}