// Based of Workroom demo provided

package persistence;


import org.json.JSONObject;

public interface Writable {

    // EFFECT: Returns this as a JSONObject
    JSONObject toJson();
}