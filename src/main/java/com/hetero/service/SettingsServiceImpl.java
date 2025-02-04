package com.hetero.service;

import com.hetero.models.Settings;
import com.hetero.repository.SettingsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
public class SettingsServiceImpl implements SettingsService {


    @Autowired
    private SettingsDao settingsDao;

    @Transactional
    @Override
    public Settings registerSettings (Settings setting) {
        settingsDao.deleteAll();
        return settingsDao.save(setting);
    }

    @Transactional(readOnly = true)
    @Override
    public Settings getSettings () {

        Settings settings = settingsDao.findFirstByOrderByIdAsc();
        if (settings == null) {
            settings = new Settings();
            settings.setAppLogo("");
            settings.setAppName("");
            settings.setBannerName("");
            settings.setCreatedAt(new Date());
            settings.setUpdatedAt(new Date());
            settingsDao.save(settings);
        }
        return settings;
    }

    @Override
    @Transactional
    public Settings updateSettingDetails (Settings updatedSetting) {
        Settings existingSettings = settingsDao.findFirstByOrderByIdAsc();

        if(existingSettings == null) {
            return settingsDao.save(updatedSetting);
        }

        existingSettings.setBannerName(updatedSetting.getBannerName());
        existingSettings.setUpdatedAt(updatedSetting.getUpdatedAt());
        existingSettings.setCreatedAt(updatedSetting.getCreatedAt());
        existingSettings.setAppName(updatedSetting.getAppName());
        existingSettings.setAppLogo(updatedSetting.getAppLogo());

        return settingsDao.save(existingSettings);
    }

}
