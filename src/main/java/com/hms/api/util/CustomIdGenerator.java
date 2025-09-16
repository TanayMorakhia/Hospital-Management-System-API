package com.hms.api.util;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Properties;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.query.NativeQuery;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class CustomIdGenerator implements IdentifierGenerator {

    private String prefix;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {

        String prefix = "";
        String tableName = "";

        // Determine prefix and table name based on entity class
        if (object.getClass().getSimpleName().equals("Patient")) {
            prefix = "PA";
            tableName = "patient";
        } else if (object.getClass().getSimpleName().equals("Doctor")) {
            prefix = "DR";
            tableName = "doctor";
        } else {
            throw new HibernateException("Unsupported entity type: " + object.getClass().getSimpleName());
        }

        try {
            // Get the next sequence number for the specific entity type
            long nextId = getNextSequenceValue(session, tableName, prefix);

            // Format the ID with leading zeros (8 digits)
            return prefix + String.format("%08d", nextId);

        } catch (Exception e) {
            throw new HibernateException("Unable to generate ID", e);
        }
    }

    private long getNextSequenceValue(SharedSessionContractImplementor session, String tableName, String prefix) {

        // Query to get the maximum existing ID for this prefix
        String maxIdQuery = "SELECT COALESCE(MAX(CAST(SUBSTRING(id, 3) AS INTEGER)), 0) FROM " + tableName +
                           " WHERE id LIKE :prefix";

        NativeQuery<BigInteger> query = session.createNativeQuery(maxIdQuery, BigInteger.class);
        query.setParameter("prefix", prefix + "%");

        BigInteger maxId = query.uniqueResult();

        if (maxId == null) {
            return 1;
        }

        return maxId.longValue() + 1;
    }
}
