package com.cozycats.cozycatsbackend.admin.Setting;

import com.cozycats.cozycatscommon.entity.Setting;
import com.cozycats.cozycatscommon.entity.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SettingRepositoryTest {
    @Autowired
    SettingRepository repo;

    @Test
    public void testCreateGeneralSettings(){
//        Setting set = new Setting("SITE_NAME", "CozyCats", SettingCategory.GENERAL);
        Setting set2 = new Setting("SITE_LOGO", "blankPicture.png", SettingCategory.GENERAL);
        Setting set3 = new Setting("COPYRIGHT", "Copyright (C) 2022 CozyCats Ltd", SettingCategory.GENERAL);
        repo.saveAll(List.of(set2, set3));

        Iterable<Setting> all = repo.findAll();
        assertThat(all).isNotNull();
    }

    @Test
    public void testCreateCurrencySettings() {
        Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
        Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
        Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
        Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
        Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
        Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

        repo.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType,
                decimalDigits, thousandsPointType));

    }

    @Test
    public void testListSettingsByCategory() {
        List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);

        settings.forEach(System.out::println);
    }

}
