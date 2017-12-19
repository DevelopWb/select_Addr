/*
 * Copyright (C) 2017 nanck
 *
 * 1999 Free Software Foundation, Inc. 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA Everyone is > > permitted to copy and distribute verbatim copies of this license document,
 * but changing it is not allowed.
 * [This is the first released version of the Lesser GPL.
 * It also counts as the successor of the GNU Library Public License, > > version 2,
 * hence the version number 2.1.]
 */

package com.choseaddrdemo.selectAddr;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nanck 2016/12/1.
 */

class Area implements Parcelable {
    private String name;
    private int id;
    private int infoId;
    private int level;
    private int fatherId;


    Area() {
    }

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    int getLevel() {
        return level;
    }

    void setLevel(int level) {
        this.level = level;
    }

    int getFatherId() {
        return fatherId;
    }

    void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    @Override
    public String toString() {
        return "Area{" +

                " name='" + name + '\'' +
                ", id=" + id +
                ", infoId=" + infoId +
                ", level=" + level +
                ", fatherId=" + fatherId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeInt(this.infoId);
        dest.writeInt(this.level);
        dest.writeInt(this.fatherId);
    }

    private Area(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
        this.infoId = in.readInt();
        this.level = in.readInt();
        this.fatherId = in.readInt();
    }

    public static final Creator<Area> CREATOR = new Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel source) {
            return new Area(source);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };
}
