package anil.sharma.bhadamandi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dragoon on 03-Nov-16.
 */
public class getNameModel {
    @SerializedName("post")
    public List<getNameList> fieldsArray;
}
