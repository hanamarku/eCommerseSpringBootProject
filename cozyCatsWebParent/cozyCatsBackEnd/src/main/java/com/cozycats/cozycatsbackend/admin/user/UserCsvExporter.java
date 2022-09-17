package com.cozycats.cozycatsbackend.admin.user;

import com.cozycats.cozycatscommon.entity.User;
import org.springframework.web.client.HttpServerErrorException;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExporter  extends AbstractExporter{
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"User ID", "E-mail", "Firstname", "Lastname", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstname", "lastname", "roles", "enabled"};
        csvWriter.writeHeader(csvHeader);
        for (User user : listUsers){
            csvWriter.write(user, fieldMapping);
        }
        csvWriter.close();
    }
}
