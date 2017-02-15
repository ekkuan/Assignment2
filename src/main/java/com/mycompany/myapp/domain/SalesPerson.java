package com.mycompany.myapp.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SalesPerson.
 */
@Entity
@Table(name = "sales_person")
public class SalesPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cust_id")
    private Integer cust_id;

    @ManyToOne
    private Car salesToCar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SalesPerson name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCust_id() {
        return cust_id;
    }

    public SalesPerson cust_id(Integer cust_id) {
        this.cust_id = cust_id;
        return this;
    }

    public void setCust_id(Integer cust_id) {
        this.cust_id = cust_id;
    }

    public Car getSalesToCar() {
        return salesToCar;
    }

    public SalesPerson salesToCar(Car car) {
        this.salesToCar = car;
        return this;
    }

    public void setSalesToCar(Car car) {
        this.salesToCar = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SalesPerson salesPerson = (SalesPerson) o;
        if (salesPerson.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, salesPerson.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SalesPerson{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", cust_id='" + cust_id + "'" +
            '}';
    }
}
