package com.example.springsocial.controller;

import com.example.springsocial.model.User;
import com.example.springsocial.repository.BookRepository;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.example.springsocial.controller.ResponseMessageHelper.*;
import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @PostMapping(value = "/uploadImageProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadImageProfile(@CurrentUser UserPrincipal userPrincipal, @RequestParam MultipartFile file) {
        Optional<User> optionalUser = userRepository.findById(userPrincipal.getId());
        if(!optionalUser.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, userPrincipal.getId()), C404);
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if(!"jpeg".equals(extension)){
            return getResponseEntity(false, IMAGE_FORMAT_INVALID, C400);
        }
        try{
            String folder = "/photos_book4u/profile/";
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folder + userPrincipal.getId() + "." + extension);
            Files.write(path, bytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        optionalUser.get().setImagePresent(true);
        userRepository.save(optionalUser.get());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/getImageProfile", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageProfile(@CurrentUser UserPrincipal userPrincipal) throws Exception{
        Optional<User> optionalUser = userRepository.findById(userPrincipal.getId());
        if(!optionalUser.isPresent()){
            throw new ResponseStatusException(C404, format(USER_NOT_FOUND, userPrincipal.getId()));
        }
        User user = optionalUser.get();
        if(!user.getImagePresent() && user.getImageUrl() == null){
            throw new ResponseStatusException(C404, format(IMAGE_NOT_FOUND, userPrincipal.getId()));
        }
        if(user.getImagePresent() && ("local".equals(user.getProvider().toString()) ||
                "facebook".equals(user.getProvider().toString()))){
            String path = "/photos_book4u/profile/" + userPrincipal.getId() + ".jpeg";
            return Files.readAllBytes(Paths.get(path));
        }
        if("facebook".equals(user.getProvider().toString())){
            URL imageUrl = new URL(user.getImageUrl());
            BufferedImage image = ImageIO.read(imageUrl);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image,"jpg",byteArrayOutputStream);
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }
        throw new ResponseStatusException(C500, format(IMAGE_NOT_FOUND, userPrincipal.getId()));
    }

    @GetMapping(value = "/getImageBook/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageBook(@PathVariable String id) throws Exception{
        long bookId;
        try {
            bookId = Long.parseLong(id);
        }catch(NumberFormatException e){
            throw new ResponseStatusException(C400, format(ID_BOOK_NOT_PARSED, id));
        }
        Optional<?> optionalBook = bookRepository.findById(bookId);
        if(!optionalBook.isPresent()){
            throw new ResponseStatusException(C404, format(BOOK_NOT_FOUND, id));
        }
        String path = "/photos_book4u/book/" + id + ".jpeg";
        return Files.readAllBytes(Paths.get(path));
    }


    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(-1);
        return multipartResolver;
    }

}
