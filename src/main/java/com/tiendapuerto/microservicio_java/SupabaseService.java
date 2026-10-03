package com.tiendapuerto.microservicio_java;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class SupabaseService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders construirHeaders() {
     HttpHeaders headers = new HttpHeaders();
     headers.set("apikey", supabaseKey.trim());
     headers.set("Authorization", "Bearer " + supabaseKey.trim());
     headers.setContentType(MediaType.APPLICATION_JSON);
     return headers;
    }

    private String tablaUrl() {
        return supabaseUrl + "/rest/v1/productos";
    }

    public List<Producto> listarTodos() {
        HttpEntity<Void> entidad = new HttpEntity<>(construirHeaders());
        ResponseEntity<Producto[]> respuesta = restTemplate.exchange(
                tablaUrl(), HttpMethod.GET, entidad, Producto[].class);
        return List.of(respuesta.getBody());
    }

    public Producto obtenerPorId(Integer id) {
        HttpEntity<Void> entidad = new HttpEntity<>(construirHeaders());
        String url = tablaUrl() + "?id=eq." + id;
        ResponseEntity<Producto[]> respuesta = restTemplate.exchange(
                url, HttpMethod.GET, entidad, Producto[].class);
        Producto[] body = respuesta.getBody();
        if (body == null || body.length == 0) return null;
        return body[0];
    }

    public Producto crear(Producto producto) {
        HttpHeaders headers = construirHeaders();
        headers.set("Prefer", "return=representation");
        HttpEntity<Producto> entidad = new HttpEntity<>(producto, headers);
        ResponseEntity<Producto[]> respuesta = restTemplate.exchange(
                tablaUrl(), HttpMethod.POST, entidad, Producto[].class);
        return respuesta.getBody()[0];
    }

    public Producto actualizar(Integer id, Producto producto) {
        if (obtenerPorId(id) == null) return null;

        HttpHeaders headers = construirHeaders();
        headers.set("Prefer", "return=representation");
        HttpEntity<Producto> entidad = new HttpEntity<>(producto, headers);
        String url = tablaUrl() + "?id=eq." + id;

        ResponseEntity<Producto[]> respuesta = restTemplate.exchange(
                url, HttpMethod.PATCH, entidad, Producto[].class);
        return respuesta.getBody()[0];
    }

    public boolean eliminar(Integer id) {
        if (obtenerPorId(id) == null) return false;

        HttpEntity<Void> entidad = new HttpEntity<>(construirHeaders());
        String url = tablaUrl() + "?id=eq." + id;
        restTemplate.exchange(url, HttpMethod.DELETE, entidad, Void.class);
        return true;
    }
}