package com.hetero.service;

import com.hetero.models.Settings;

import java.util.Map;

public interface SettingsService {


    Settings registerSettings(Settings setting);
    Settings getSettings();
    Settings updateSettingDetails(Settings updatedSetting);

}
