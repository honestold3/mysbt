package org.wq.mysbt.cassandra;


import java.io.Serializable;

/**
 * Created by wq on 14/11/24.
 */
public class User implements Serializable{

    private Integer user_id;

    private String fname;

    private String lname;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
