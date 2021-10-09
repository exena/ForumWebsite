package gameinfoweb.gameinfo.imageuploader;


import com.google.gson.JsonObject;
import gameinfoweb.gameinfo.model.Image;
import gameinfoweb.gameinfo.repository.ImageRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
@RequestMapping
public class ImageUploader {

    @Autowired
    private ImageRepository imageRepository;

    @PostMapping(value="/uploadSummernoteImageFile", produces = "application/json; charset=utf8")
    @ResponseBody
    public JsonObject uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request ) {

        JsonObject jsonObject = new JsonObject();
        String fileRoot = "/home/osusml2135/summernote_image/";
        //"C:\\summernote_image\\"
        //"/home/osusml2135/summernote_image/"
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자

        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/summernoteImage/"+savedFileName);
            jsonObject.addProperty("responseCode", "success");
            Image image= new Image();
            image.setOriginalname(originalFileName);
            image.setFilename(savedFileName);
            imageRepository.save(image);

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            imageRepository.delete(imageRepository.findByFilename(savedFileName).get(0));
            e.printStackTrace();
        }

        return jsonObject;
    }
}
