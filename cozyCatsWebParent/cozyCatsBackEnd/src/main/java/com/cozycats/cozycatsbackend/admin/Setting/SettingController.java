package com.cozycats.cozycatsbackend.admin.Setting;

import com.cozycats.cozycatsbackend.admin.Currency.CurrencyRepository;
import com.cozycats.cozycatsbackend.admin.FileUploadUtil;
import com.cozycats.cozycatscommon.entity.Currency;
import com.cozycats.cozycatscommon.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingController {
    @Autowired
    private SettingService settingService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/settings")
    public String listAll(Model model){
        List<Setting> listSettings = settingService.listAllSettings();
        List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();
        model.addAttribute("listSettings", listSettings);
        model.addAttribute("listCurrencies", listCurrencies);
        model.addAttribute("localDateTime", LocalDateTime.now());

        for(Setting setting : listSettings){
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "settings/settings";
    }


    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
                                      HttpServletRequest request, RedirectAttributes ra) throws IOException {
        GeneralSettingBag settingBag = settingService.getGeneralSettings();

        saveSiteLogo(multipartFile, settingBag);
        saveCurrencySymbol(request, settingBag);

        updateSettingValuesFromForm(request, settingBag.list());

        ra.addFlashAttribute("message", "General settings have been saved.");

        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException, IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/" + fileName;
            settingBag.updateSiteLogo(value);

            String uploadDir = "../site-logo/";
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findByIdResult = currencyRepository.findById(currencyId);

        if (findByIdResult.isPresent()) {
            Currency currency = findByIdResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
        for (Setting setting : listSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }
        settingService.saveAll(listSettings);
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes ra){
        List<Setting> mailServerSetting = settingService.getMailServerSettings();
        updateSettingValuesFromForm(request, mailServerSetting);

        ra.addFlashAttribute("message", "Mail server setting have been saved");

        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplateSetttings(HttpServletRequest request, RedirectAttributes ra) {
        List<Setting> mailTemplateSettings = settingService.getMailTemplateSettings();
        updateSettingValuesFromForm(request, mailTemplateSettings);

        ra.addFlashAttribute("message", "Mail template settings have been saved");

        return "redirect:/settings#mailTemplates";
    }

    @PostMapping("/settings/save_payment")
    public String savePaymentSetttings(HttpServletRequest request, RedirectAttributes ra) {
        List<Setting> paymentSettings = settingService.getPaymentSettings();
        updateSettingValuesFromForm(request, paymentSettings);

        ra.addFlashAttribute("message", "Payment settings have been saved");

        return "redirect:/settings#payment";
    }

}
