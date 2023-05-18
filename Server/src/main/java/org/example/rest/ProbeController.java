package org.example.rest;
import org.example.entity.Probe;
import org.example.repo.ProbesDBRepo;
import org.example.services.Service;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@RestController
@RequestMapping("/probes")
public class ProbeController {
    private final List<Probe> probes = new ArrayList<>();
    private Service service;
    private int nextId = 1;

    public ProbeController() {
        Properties properties = new Properties();
        try{
            properties.load(new FileReader("bd.config"));
        }catch (Exception ignored){

        }
        service = new Service(properties);
    }

    @GetMapping
    public List<Probe> getAllProbes() {
        try {
            return service.getProbes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public Probe getProbeById(@PathVariable int id) {
        try {
            return service.getProbes().stream()
                    .filter(probe -> probe.getCod() == id)
                    .findFirst()
                    .orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public int createProbe(@RequestBody Probe probe) {
        nextId = getMax();
        probe.setCod(nextId+1);
        service.addProbe(probe);
        return probe.getCod();
    }

    private int getMax() {
        List<Probe> probes = null;
        try {
            probes = service.getProbes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int id = 0;
        for (Probe probe:
             probes) {
            if(id < probe.getCod())
                id = probe.getCod();
        }
        return id;
    }

    @PutMapping("/{id}")
    public void updateProbe(@PathVariable int id, @RequestBody Probe updatedProbe) {
        Probe probe = new Probe(id, updatedProbe.getName());
        service.update(probe);
    }

    @DeleteMapping("/{id}")
    public void deleteProbe(@PathVariable int id) {
        service.remove(id);
    }
}
