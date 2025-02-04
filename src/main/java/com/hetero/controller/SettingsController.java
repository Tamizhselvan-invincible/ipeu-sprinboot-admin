package com.hetero.controller;


import com.hetero.models.Settings;
import com.hetero.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;


    @GetMapping
    public ResponseEntity<Settings> getSettingsDetail(){
        Settings settings = settingsService.getSettings();
        return ResponseEntity.ok(settings);
    }

    @PutMapping
    public ResponseEntity<Settings> updateSettingsDetail(@RequestBody Settings settings){
        return ResponseEntity.ok(settingsService.updateSettingDetails(settings));
    }

    @PostMapping
    public ResponseEntity<Settings> addSettingsDetail(@RequestBody Settings settings){
        return ResponseEntity.ok(settingsService.registerSettings(settings));
    }

}
