package com.theironyard.controllers;

import com.theironyard.entities.AnonFile;
import com.theironyard.services.AnonFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cameronoakley on 11/18/15.
 */
@RestController
public class AnonFileController {
    @Autowired
    AnonFileRepository files;

    @RequestMapping("/files")
    public List<AnonFile> getFiles (){
        return (List<AnonFile>) files.findAll();
    }

    @RequestMapping("/upload")
    public void upload(MultipartFile file, HttpServletResponse response, boolean permanant,String comment) throws Exception {
        File f = File.createTempFile("file", file.getOriginalFilename(), new File("public"));
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(file.getBytes());

        AnonFile anonFile = new AnonFile();
        anonFile.originalName = file.getOriginalFilename();
        anonFile.name = f.getName();
        anonFile.permanent = permanant;
        anonFile.comment = comment;
        files.save(anonFile);

        if (files.findByPermanent(false).size()>5){
            List<AnonFile> fileList = (List<AnonFile>) files.findByPermanent(false);
            AnonFile fileDelete = fileList.get(0);
            if (!fileDelete.permanent){
                File hateNaming = new File("public", fileDelete.name);
                files.delete(fileDelete);
                hateNaming.delete();
            }
        }





        response.sendRedirect("/");
    }
}

