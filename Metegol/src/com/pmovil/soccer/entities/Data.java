package com.pmovil.soccer.entities;

import java.io.Serializable;

public class Data implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Data))
            return false;
        Data other = (Data) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
