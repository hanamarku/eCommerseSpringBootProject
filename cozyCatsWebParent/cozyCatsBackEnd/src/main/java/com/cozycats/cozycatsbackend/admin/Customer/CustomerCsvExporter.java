package com.cozycats.cozycatsbackend.admin.Customer;

import com.cozycats.cozycatsbackend.admin.AbstractExporter;
import com.cozycats.cozycatscommon.entity.Brand;
import com.cozycats.cozycatscommon.entity.Customer;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CustomerCsvExporter extends AbstractExporter {
    public void export(List<Customer> listCustomers, HttpServletResponse response)
            throws IOException {
        super.setResponseHeader(response, "text/csv", ".csv", "customers_");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Customer ID", "Name", "LastName", "Address Line 1" , "Address Line 2", "Postal Code", "State" , "Phone Number"};
        String[] fieldMapping = {"id", "firstName", "lastName" , "addressLine1", "addressLine2", "postalCode", "state", "phoneNumber"};

        csvWriter.writeHeader(csvHeader);
        for (Customer customer : listCustomers){
            csvWriter.write(customer, fieldMapping);
        }
        csvWriter.close();
    }
}
