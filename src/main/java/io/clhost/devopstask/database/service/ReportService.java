package io.clhost.devopstask.database.service;

import io.clhost.devopstask.database.HibernateUtil;
import io.clhost.devopstask.database.Report;
import io.clhost.devopstask.database.fields.ReportFields;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class ReportService implements GenericEntityService<Report, String> {
    @Override
    public List<Report> getAll() throws Exception {
        return null;
    }

    @Override
    public Report get(String s, String column) throws Exception {
        Session session = null;
        Report report = null;
        NativeQuery<Report> nativeQuery;

        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();

            switch (column) {
                case ReportFields.ID:
                    nativeQuery = session.createNativeQuery(
                            "select * from" +
                                    ReportFields.TABLE_NAME + " where " + ReportFields.ID + " = :rid", Report.class);
                    nativeQuery.setParameter("rid", Long.parseLong(s));
                    report = nativeQuery.getSingleResult();
                    break;
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return report;
    }

    @Override
    public void save(Report report) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();

            session.save(report);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void delete(String s, String column) throws Exception {
        throw new UnsupportedOperationException("Delete is still unsupported.");
    }
}
