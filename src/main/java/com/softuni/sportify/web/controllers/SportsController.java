package com.softuni.sportify.web.controllers;

import com.softuni.sportify.domain.models.binding_models.ImageCreateBindingModel;
import com.softuni.sportify.domain.models.binding_models.ImageEditBindingModel;
import com.softuni.sportify.domain.models.binding_models.SportCreateBindingModel;
import com.softuni.sportify.domain.models.binding_models.SportEditBindingModel;
import com.softuni.sportify.domain.models.service_models.ImageServiceModel;
import com.softuni.sportify.domain.models.service_models.SportServiceModel;
import com.softuni.sportify.domain.models.view_models.ImageViewModel;
import com.softuni.sportify.domain.models.view_models.SportViewModel;
import com.softuni.sportify.exceptions.CreateException;
import com.softuni.sportify.exceptions.DeleteException;
import com.softuni.sportify.exceptions.ReadException;
import com.softuni.sportify.exceptions.UpdateException;
import com.softuni.sportify.services.ImageService;
import com.softuni.sportify.services.SportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.softuni.sportify.constants.AuthConstants.HAS_ROLE_ADMIN;
import static com.softuni.sportify.constants.SportsControllerConstants.*;

@Controller
@RequestMapping("/sports")
public class SportsController {

    private final ModelMapper modelMapper;
    private final SportService sportService;
    private final ImageService imageService;

    @Autowired
    public SportsController(ModelMapper modelMapper,
                            SportService sportService,
                            ImageService imageService) {
        this.modelMapper = modelMapper;
        this.sportService = sportService;
        this.imageService = imageService;
    }

    @GetMapping("/create-sport")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView addNewSport(ModelAndView modelAndView) {

        modelAndView.addObject("sportCreateBindingModel", new SportCreateBindingModel());
        modelAndView.setViewName(VIEW_CREATE_SPORT);
        return modelAndView;
    }

    @PostMapping("/create-sport")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView editImageConfirmed(
            @Valid
            @ModelAttribute SportCreateBindingModel sportCreateBindingModel,
            BindingResult sportBindingResult,
            ModelAndView modelAndView) throws IOException, CreateException {

        if(sportBindingResult.hasErrors()) {
            modelAndView.addObject("sportCreateBindingModel", sportCreateBindingModel);
            modelAndView.setViewName(VIEW_CREATE_SPORT);
            return modelAndView;
        }

        SportServiceModel sportServiceModel = this.modelMapper
                .map(sportCreateBindingModel, SportServiceModel.class);
        ImageServiceModel iconImageServiceModel = this.imageService
                .createImageMultipartFile(sportCreateBindingModel.getIconImage(),sportCreateBindingModel.getName());
        SportServiceModel newSportServiceModel = this.sportService
                .createSport(sportServiceModel, iconImageServiceModel);

        modelAndView.setViewName(REDIRECT_TO_SPORT_DETAILS + newSportServiceModel.getId());
        return modelAndView;
    }

    @GetMapping("/sport-details/{id}")
    public ModelAndView sportDetails(
            @PathVariable String id,
            ModelAndView modelAndView) throws ReadException {

        SportServiceModel sportServiceModel = this.sportService.findByID(id);
        SportViewModel sportViewModel = this.modelMapper.map(sportServiceModel, SportViewModel.class);
        modelAndView.addObject("sportViewModel", sportViewModel);
        modelAndView.addObject("imageCreateBindingModel", new ImageCreateBindingModel());

        modelAndView.setViewName(VIEW_SPORT_DETAILS);
        return modelAndView;
    }

    @GetMapping("/guests-sport-details/{id}")
    public ModelAndView guestsSportDetails(
            @PathVariable String id,
            ModelAndView modelAndView) throws ReadException {

        SportServiceModel sportServiceModel = this.sportService.findByID(id);
        SportViewModel sportViewModel = this.modelMapper.map(sportServiceModel, SportViewModel.class);
        modelAndView.addObject("sportViewModel", sportViewModel);

        modelAndView.setViewName(VIEW_GUESTS_SPORT_DETAILS);
        return modelAndView;
    }

    @PostMapping("/edit-description/{id}")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView editDescription(
            @PathVariable("id") String sportID,
            SportEditBindingModel sportEditBindingModel,
            ModelAndView modelAndView) throws ReadException, UpdateException {

        SportServiceModel sportServiceModel = this.sportService.findByID(sportID);
        sportServiceModel.setSportDescription(sportEditBindingModel.getSportDescription());
        SportServiceModel updatedSportServiceModel = this.sportService.updateSportDescription(sportServiceModel);
        SportViewModel sportViewModel = this.modelMapper.map(updatedSportServiceModel, SportViewModel.class);

        modelAndView.addObject("sportViewModel", sportViewModel);

        modelAndView.setViewName(REDIRECT_TO_SPORT_DETAILS + sportID);
        return modelAndView;
    }

    @PostMapping("/add-sport-images/{id}")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView addSportImages(
            @PathVariable("id") String sportID,
            @Valid
            @ModelAttribute ImageCreateBindingModel imageCreateBindingModel,
            BindingResult imageBindingResult,
            ModelAndView modelAndView) throws IOException, ReadException, CreateException, UpdateException {

        SportServiceModel sportServiceModel = this.sportService.findByID(sportID);

        if(imageBindingResult.hasErrors()) {
            SportViewModel sportViewModel = this.modelMapper.map(sportServiceModel, SportViewModel.class);
            modelAndView.addObject("sportViewModel", sportViewModel);
            modelAndView.addObject("imageCreateBindingModel", imageCreateBindingModel);

            modelAndView.setViewName(VIEW_SPORT_DETAILS);
            return modelAndView;
        }

        ImageServiceModel imageServiceModel = this.imageService
                .createImageMultipartFile(imageCreateBindingModel.getImage(), imageCreateBindingModel.getName());
        SportServiceModel updatedSportServiceModel = this.sportService
                .addSportImage(sportServiceModel, imageServiceModel);
        SportViewModel sportViewModel = this.modelMapper.map(updatedSportServiceModel, SportViewModel.class);

        modelAndView.addObject("sportViewModel", sportViewModel);

        modelAndView.setViewName(REDIRECT_TO_SPORT_DETAILS + sportID);
        return modelAndView;
    }

    @GetMapping("/show-all-sports")
    public ModelAndView showAllSports(ModelAndView modelAndView) {

        List<SportViewModel> sportViewModels = this.sportService.findAllSports()
                .stream()
                .map(s -> this.modelMapper.map(s, SportViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("sportViewModels", sportViewModels);

        modelAndView.setViewName(VIEW_SHOW_ALL_SPORTS);
        return modelAndView;
    }

    @GetMapping("/edit-sport-image/{sportID}/{imageID}")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView editSportImage(
            @PathVariable("sportID") String sportID,
            @PathVariable("imageID") String imageID,
            ModelAndView modelAndView) throws ReadException {

        SportViewModel sportViewModel = this.modelMapper
                .map(this.sportService.findByID(sportID), SportViewModel.class);
        ImageServiceModel imageServiceModel = this.imageService.findImageByID(imageID);
        ImageViewModel imageViewModel = this.modelMapper.map(imageServiceModel, ImageViewModel.class);

        modelAndView.addObject("sportViewModel", sportViewModel);
        modelAndView.addObject("imageViewModel", imageViewModel);
        modelAndView.addObject("imageEditBindingModel", new ImageEditBindingModel());

        modelAndView.setViewName(VIEW_EDIT_SPORT_IMAGE);
        return modelAndView;
    }

    @PostMapping("/edit-sport-image/{sportID}")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView editSportImageConfirmed(
            @PathVariable("sportID") String sportID,
            @Valid
            @ModelAttribute ImageEditBindingModel imageEditBindingModel,
            BindingResult imageBindingResult,
            ModelAndView modelAndView) throws IOException, UpdateException, ReadException {

        if(imageBindingResult.hasErrors()) {
            SportViewModel sportViewModel = this.modelMapper
                    .map(this.sportService.findByID(sportID), SportViewModel.class);
            ImageViewModel imageViewModel = this.modelMapper.map(imageEditBindingModel, ImageViewModel.class);
            modelAndView.addObject("sportViewModel", sportViewModel);
            modelAndView.addObject("imageViewModel", imageViewModel);
            modelAndView.addObject("imageEditBindingModel", imageEditBindingModel);

            modelAndView.setViewName(VIEW_EDIT_SPORT_IMAGE);
            return modelAndView;
        }

        this.imageService.editImage(this.modelMapper.map(imageEditBindingModel, ImageServiceModel.class));

        modelAndView.setViewName(REDIRECT_TO_SPORT_DETAILS + sportID);
        return modelAndView;
    }

    @PostMapping("/delete-sport-image/{sportID}/{imageID}")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView deleteSportImage(
            @PathVariable("sportID") String sportID,
            @PathVariable("imageID") String imageID,
            ModelAndView modelAndView) throws Exception, UpdateException, DeleteException {

        this.sportService.deleteSportImage(sportID, imageID);
        this.imageService.deleteImage(imageID);

        modelAndView.setViewName(REDIRECT_TO_SPORT_DETAILS + sportID);
        return modelAndView;
    }

    @PostMapping("/delete-sport/{sportID}")
    @PreAuthorize(HAS_ROLE_ADMIN)
    public ModelAndView deleteSport(
            @PathVariable("sportID") String sportID,
            ModelAndView modelAndView) throws Exception, UpdateException, DeleteException, ReadException {

        SportServiceModel sportServiceModel = this.sportService.findByID(sportID);
        this.sportService.deleteSport(sportServiceModel);

        modelAndView.setViewName(REDIRECT_TO_SHOW_ALL_SPORTS);
        return modelAndView;
    }
}
