package com.snapxeats.ui.foodstack;

import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 27/2/18.
 */
@Getter
@Setter
public class FoodStackData{
    private String name;
    private List<String> url;
    private String id;
    private String dishId;

    public FoodStackData(){}

    public FoodStackData(String name, String id, List<String> url, String dishId) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodStackData)) return false;
        FoodStackData that = (FoodStackData) o;
        return  Objects.equals(getId(), that.getId()) &&
                Objects.equals(getDishId(), that.getDishId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getUrl(), getId(), getDishId());
    }

    @Override
    public String toString() {
        return "FoodStackData{" +
                "name='" + name + '\'' +
                ", url=" + url +
                ", id='" + id + '\'' +
                ", dishId='" + dishId + '\'' +
                '}';
    }
}
