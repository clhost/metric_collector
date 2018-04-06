package io.clhost.devopstask.database.fields;

import io.clhost.devopstask.database.Report;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReportFields {
    public static final String TABLE_NAME = "report_table";
    public static final String ID = "id";
    public static final String TIME = "time";
    public static final String RAM = "ram";
    public static final String DISK = "disk";
    public static final String CPU = "cpu";

    static {
        Class c = Report.class;

        Field[] reportFields = c.getDeclaredFields();
        Field[] thisFields = ReportFields.class.getDeclaredFields();

        List<String> reportFieldsList = new ArrayList<>();
        List<String> thisFieldsList = new ArrayList<>();

        for (Field field : thisFields) {
            field.setAccessible(true);
            try {
                if (!field.get(ReportFields.class).equals(ReportFields.TABLE_NAME)) {
                    thisFieldsList.add((String) field.get(ReportFields.class));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Field field : reportFields) {
            if (field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(Embedded.class)) {
                continue;
            }

            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                reportFieldsList.add(column.name());
            }
        }

        System.out.println(reportFieldsList);
        System.out.println(thisFieldsList);

        if (!reportFieldsList.equals(thisFieldsList)) {
            throw new IllegalStateException("ReportFields.class fields are not equal to fields which exists in " +
                    "database.");
        }
    }
}
