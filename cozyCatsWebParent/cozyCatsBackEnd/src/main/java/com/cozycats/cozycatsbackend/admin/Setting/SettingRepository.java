package com.cozycats.cozycatsbackend.admin.Setting;

import com.cozycats.cozycatscommon.entity.Setting;
import com.cozycats.cozycatscommon.entity.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {

    public List<Setting> findByCategory(SettingCategory category);
}
