package com.example.projectworkv02.internet;


import org.json.JSONObject;

// interfaccia necessaria ad attendere l'avvenuto download del JSONObject prima di far continuare il codice
public interface ServerCallback {
    void onSuccess(JSONObject result);
}
