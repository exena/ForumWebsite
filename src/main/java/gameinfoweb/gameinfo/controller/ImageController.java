package gameinfoweb.gameinfo.controller;

import gameinfoweb.gameinfo.model.Board;
import gameinfoweb.gameinfo.model.Image;
import gameinfoweb.gameinfo.repository.ImageRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;


@Controller
@RequestMapping("/summernoteImage")
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping(value = "/{imagename}",produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<byte[]> getImage(@PathVariable("imagename") String imagename) throws IOException {
        InputStream imageStream = new FileInputStream("/home/osusml2135/summernote_image/" + imagename);
        //"/home/osusml2135/summernote_image/"
        //"C:/summernote_image/"
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        List<Image> imageList = imageRepository.findByFilename(imagename);
        if (imageList.size() != 0) {
            imagename = imageList.get(0).getOriginalname();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename(imagename, StandardCharsets.UTF_8)
                .build());
        return new ResponseEntity<byte[]>(imageByteArray,headers,HttpStatus.OK);
    }
}
