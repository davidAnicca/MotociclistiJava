package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Main {
    private static final String BASE_URL = "http://localhost:8080/probes";

    private static String executeRequest(HttpRequestBase request) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        }
        return null;
    }

    public static String getAllProbes() throws IOException {
        HttpGet request = new HttpGet(BASE_URL);
        return executeRequest(request);
    }

    public static String getProbeById(int id) throws IOException {
        HttpGet request = new HttpGet(BASE_URL + "/" + id);
        return executeRequest(request);
    }

    public static String createProbe(String name) throws IOException {
        HttpPost request = new HttpPost(BASE_URL);
        StringEntity entity = new StringEntity("{\"name\": \"" + name + "\"}");
        entity.setContentType("application/json");
        request.setEntity(entity);
        return executeRequest(request);
    }

    public static void updateProbe(int id, String newName) throws IOException {
        HttpPut request = new HttpPut(BASE_URL + "/" + id);
        StringEntity entity = new StringEntity("{\"name\": \"" + newName + "\"}");
        entity.setContentType("application/json");
        request.setEntity(entity);
        executeRequest(request);
    }

    public static void deleteProbe(int id) throws IOException {
        HttpDelete request = new HttpDelete(BASE_URL + "/" + id);
        executeRequest(request);
    }

    public static void main(String[] args) {
        try {

            String allProbesResponse = getAllProbes();
            System.out.println("All Probes: " + allProbesResponse);


            String probeByIdResponse = getProbeById(1);
            System.out.println("Probe by ID: " + probeByIdResponse);


            String createProbeResponse = createProbe("New Probe2");
            System.out.println("Create Probe: " + createProbeResponse);

            updateProbe(1, "Updated Probe");

            allProbesResponse = getAllProbes();
            System.out.println("All Probes: " + allProbesResponse);
            deleteProbe(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
