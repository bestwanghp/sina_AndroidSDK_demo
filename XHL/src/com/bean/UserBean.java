package com.bean;

public class UserBean {

    private String uid;//用户UID
    private String screen_name;//用户昵称
    private String location;//用户所在地
    private String description;//描述
    private String url;
    private String profile_image;//用户头像地址，50×50像素
    private String gender;//性别，m：男、f：女、n：未知

    @Override
    public String toString() {
        return "UserBean [uid=" + uid + ", screen_name=" + screen_name
                + ", location=" + location+", description ="+description+
                ", profile_image=" + profile_image + ", gender="
                + gender + "]";
    }

    public UserBean() {

    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfile_image() {
		return profile_image;
	}

	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}

	
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

  
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    

}