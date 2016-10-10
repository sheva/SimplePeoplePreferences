package com.sheva.api.providers.xml;

import com.sheva.data.Food;
import com.sheva.data.Person;

import javax.ws.rs.WebApplicationException;
import javax.xml.bind.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates marshaller instances with defined context.
 *
 * Created by Sheva on 10/4/2016.
 */
public class JaxbMarshallerProvider {

    private static final Logger logger = Logger.getLogger(JaxbMarshallerProvider.class.getName());

    private static JaxbMarshallerProvider instance;

    private final JAXBContext jaxbContext;

    private JaxbMarshallerProvider() {
        try {
            jaxbContext = JAXBContext.newInstance(Food.class, Person.class);
        } catch (JAXBException jaxbException) {
            logger.log(Level.SEVERE, jaxbException.getMessage(), jaxbException);
            throw new WebApplicationException(jaxbException.getMessage(), jaxbException);
        }
    }

    public static synchronized JaxbMarshallerProvider getInstance() {
        if (instance == null) {
            instance = new JaxbMarshallerProvider();
        }
        return instance;
    }

    public Marshaller getMarshaller() throws JAXBException {
        return jaxbContext.createMarshaller();
    }

    public Unmarshaller getUnmarshaller() throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setEventHandler((ValidationEvent event) -> false);
        return unmarshaller;
    }
}