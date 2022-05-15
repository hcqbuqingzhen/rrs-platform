package com.springboot.demo.bean;

import java.util.List;
import java.util.Objects;

public class BeanList {
    private List<Mybean> list;

    public BeanList(List<Mybean> list) {
        this.list = list;
    }
    public BeanList() {

    }

    public List<Mybean> getList() {
        return list;
    }

    public void setList(List<Mybean> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanList beanList = (BeanList) o;
        return Objects.equals(list, beanList.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
