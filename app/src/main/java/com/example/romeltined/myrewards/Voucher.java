package com.example.romeltined.myrewards;

/**
 * Created by romeltined on 8/22/2017.
 */

public class Voucher {
    public int id;
    public String name;

    private boolean checked = false;

    public Voucher(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public String toString()
    {
        return name;
    }

    public void toggleChecked()
    {
        checked = !checked;
    }
}


