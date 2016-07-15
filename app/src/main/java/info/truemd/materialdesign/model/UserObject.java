package info.truemd.materialdesign.model;

/**
 * Created by yashvardhansrivastava on 02/03/16.
 */
public class UserObject {

    String authentication_token;
    String created_at;

    String email;
    String mobile;
    String name;

    String otp_token;
    String status;

    String user_type;

    public UserObject()
    {

    }

    public UserObject(String authentication_token, String created_at, String email, String mobile, String name, String otp_token, String status, String user_type) {
        this.authentication_token = authentication_token;
        this.created_at = created_at;
        this.email = email;
        this.mobile = mobile;
        this.name = name;
        this.otp_token = otp_token;
        this.status = status;
        this.user_type = user_type;
    }

    public String getAuthentication_token() {
        return authentication_token;
    }

    public void setAuthentication_token(String authentication_token) {
        this.authentication_token = authentication_token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtp_token() {
        return otp_token;
    }

    public void setOtp_token(String otp_token) {
        this.otp_token = otp_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
