package com.neusoft.my12603.bean;

import java.io.Serializable;

public class Passenger implements Serializable {
	private String id;
	private String name;
	private String idType;
	private String tel;
	private Seat seat;
	private String type;

	public void setId(String id) {
		this.id = id;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getIdType() {
		return idType;
	}

	public String getName() {
		return name;
	}

	public Seat getSeat() {
		return seat;
	}

	public String getTel() {
		return tel;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Passenger [id=" + id + ", name=" + name + ", idType=" + idType
				+ ", tel=" + tel + ", seat=" + seat + ", type=" + type + "]";
	}

}
