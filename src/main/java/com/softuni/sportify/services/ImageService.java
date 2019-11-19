package com.softuni.sportify.services;

import com.softuni.sportify.domain.models.service_models.ImageServiceModel;
import org.springframework.stereotype.Service;

@Service
public interface ImageService {

    ImageServiceModel createImage(ImageServiceModel imageServiceModel);

    ImageServiceModel findImageByName(String name);
}