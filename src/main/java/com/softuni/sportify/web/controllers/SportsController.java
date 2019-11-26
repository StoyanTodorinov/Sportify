package com.softuni.sportify.web.controllers;

import com.softuni.sportify.domain.models.binding_models.ImageCreateBindingModel;
import com.softuni.sportify.domain.models.binding_models.SportAddNewBindingModel;
import com.softuni.sportify.domain.models.service_models.ImageServiceModel;
import com.softuni.sportify.domain.models.service_models.SportServiceModel;
import com.softuni.sportify.services.CloudinaryService;
import com.softuni.sportify.services.ImageService;
import com.softuni.sportify.services.SportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static com.softuni.sportify.constants.AuthConstants.HAS_ROLE_ADMIN;
import static com.softuni.sportify.constants.SportsControllerConstants.*;

@Controller
@RequestMapping("/sports")
public class SportsController {

    private final ModelMapper modelMapper;
    private final SportService sportService;
    private final ImageService imageService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public SportsController(ModelMapper modelMapper,
                            SportService sportService,
                            ImageService imageService,
                            CloudinaryService cloudinaryService) {
        this.modelMapper = modelMapper;
        this.sportService = sportService;
        this.imageService = imageService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/add-new-sport")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView addNewSport(ModelAndView modelAndView) {

        modelAndView.setViewName(VIEW_ADD_NEW_SPORT);
        return modelAndView;
    }

    @PostMapping("/add-new-sport")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView editImageConfirmed(@ModelAttribute SportAddNewBindingModel sportAddNewBindingModel,
                                           ModelAndView modelAndView) throws IOException {

        SportServiceModel sportServiceModel = this.sportService
                .addNewSport(this.modelMapper.map(sportAddNewBindingModel, SportServiceModel.class));
        modelAndView.setViewName(REDIRECT_TO_CREATE_SPORT_IMAGE + sportServiceModel.getId());
        return modelAndView;
    }

    @GetMapping("/create-sport-image/{id}")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView createSportImage(@PathVariable String id,
                                         @ModelAttribute ImageCreateBindingModel imageCreateBindingModel,
                                         ModelAndView modelAndView) {

        modelAndView.addObject("imageCreateBindingModel", imageCreateBindingModel);
        SportServiceModel sportServiceModel = this.sportService.findByID(id);
        modelAndView.addObject("sportServiceModel", sportServiceModel);
        modelAndView.setViewName(VIEW_CREATE_SPORT_IMAGE);
        return modelAndView;
    }

    @PostMapping("/create-sport-image/{id}")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView createSportImageConfirm(@PathVariable String id,
                                                @ModelAttribute ImageCreateBindingModel imageCreateBindingModel,
                                                ModelAndView modelAndView) throws IOException {

        ImageServiceModel imageServiceModel = this.modelMapper.map(imageCreateBindingModel, ImageServiceModel.class);
        imageServiceModel.setImageURL(this.cloudinaryService.uploadImage(imageCreateBindingModel.getImage()));
        ImageServiceModel newImageServiceModel = this.imageService.createImage(imageServiceModel);

        SportServiceModel sportServiceModel = this.sportService
                .addSportImage(id, this.imageService.createImage(newImageServiceModel));

        modelAndView.addObject("sportServiceModel", sportServiceModel);

        modelAndView.setViewName(REDIRECT_TO_CREATE_SPORT_IMAGE + id);
        return modelAndView;
    }

//    @PostMapping("/delete-image")
//    @PreAuthorize(HAS_ROLE_ADMIN)
//    public ModelAndView deleteImage(@RequestParam(name = "sportID") String sportID,
//                                    @RequestParam(name = "imageID") String imageID,
//                                    ModelAndView modelAndView) throws Exception {
//        this.sportService.deleteImage(sportID, imageID);
//        modelAndView.setViewName(REDIRECT_TO_CREATE_SPORT_IMAGE + sportID);
//        return modelAndView;
//    }


    @GetMapping("/list-all")
    public ModelAndView listAllSports(ModelAndView modelAndView) {
        modelAndView.setViewName(VIEW_ALL_IMAGES_BY_SPORT);
        return modelAndView;
    }
}
