package com.softuni.sportify.domain.models.service_models;

import java.util.List;

public class ThemeServiceModel extends BaseServiceModel {

    private String name;
    private ImageServiceModel iconImage;
    private List<ImageServiceModel> indexCarouselImages;
    private List<ImageServiceModel> homeCarouselImages;
    private List<ImageServiceModel> adminPanelImages;
    private Boolean active;

    public ThemeServiceModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageServiceModel getIconImage() {
        return iconImage;
    }

    public void setIconImage(ImageServiceModel iconImage) {
        this.iconImage = iconImage;
    }

    public List<ImageServiceModel> getIndexCarouselImages() {
        return indexCarouselImages;
    }

    public void setIndexCarouselImages(List<ImageServiceModel> indexCarouselImages) {
        this.indexCarouselImages = indexCarouselImages;
    }

    public List<ImageServiceModel> getHomeCarouselImages() {
        return homeCarouselImages;
    }

    public void setHomeCarouselImages(List<ImageServiceModel> homeCarouselImages) {
        this.homeCarouselImages = homeCarouselImages;
    }

    public List<ImageServiceModel> getAdminPanelImages() {
        return adminPanelImages;
    }

    public void setAdminPanelImages(List<ImageServiceModel> adminPanelImages) {
        this.adminPanelImages = adminPanelImages;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}