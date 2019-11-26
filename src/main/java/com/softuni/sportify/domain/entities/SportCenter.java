package com.softuni.sportify.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sport_centers")
public class SportCenter extends BaseEntity {

    private String name;
    private Address address;
    private String sportCenterDescription;
    private List<Image> sportCenterImages;
    private List<Sport> sports;
    private List<Event> events;

    public SportCenter() {
        this.sportCenterImages = new ArrayList<>();
        this.sports = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="sport_center_id")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Column(name = "sport_center_description", nullable = false, columnDefinition = "TEXT")
    public String getSportCenterDescription() {
        return sportCenterDescription;
    }

    public void setSportCenterDescription(String sportCenterDescription) {
        this.sportCenterDescription = sportCenterDescription;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "sport_center_images",
            joinColumns = @JoinColumn(name = "sport_center_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    public List<Image> getSportCenterImages() {
        return sportCenterImages;
    }

    public void setSportCenterImages(List<Image> sportCenterImages) {
        this.sportCenterImages = sportCenterImages;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "sport_center_sports",
            joinColumns = @JoinColumn(name = "sport_center_id"),
            inverseJoinColumns = @JoinColumn(name = "sport_id")
    )
    public List<Sport> getSports() {
        return sports;
    }

    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sport_center_id", nullable = false)
    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}
